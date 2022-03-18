package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.dto.*
import com.keplerworks.f1tipper.exception.AccessForbiddenException
import com.keplerworks.f1tipper.exception.BetItemStillOpenException
import com.keplerworks.f1tipper.exception.BetNotFoundException
import com.keplerworks.f1tipper.model.*
import com.keplerworks.f1tipper.repository.BetItemRepository
import com.keplerworks.f1tipper.repository.BetRepository
import com.keplerworks.f1tipper.toPositions
import com.keplerworks.f1tipper.type.BetItemStatus
import com.keplerworks.f1tipper.type.BetItemType
import com.keplerworks.f1tipper.type.BetType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*

@Service
class BetService @Autowired constructor(private val betRepo: BetRepository,
                                        private val betItemRepo: BetItemRepository,
                                        private val raceService: RaceService,
                                        private val positionService: PositionService,
                                        private val betSubjectService: BetSubjectService,
                                        //private val driverService: DriverService,
                                        private val userService: FormulaUserService,
                                        private val resultService: ResultService) {

    fun getBetsByLeague(username: String, leagueId: Long): List<BetDTO> {
        val userId: Long = userService.getUser(username).id
        val betDTOs: MutableList<BetDTO> = mutableListOf()
        val bets = getBetsByLeague(userId, leagueId)
        val raceIds = mutableListOf<Long>()
        bets.forEach { raceIds.add(it.race.id) }
        val races: List<Race> = raceService.getAllRaces()

        if (bets.none { it.type == BetType.CHAMPIONSHIP.value }) {
            bets.add(Bet(race = races.first(), userId = userId, type = BetType.CHAMPIONSHIP.value, leagueId = leagueId))
        }

        races.forEach {
            if (!raceIds.contains(it.id)) {
                bets.add(Bet(race = it, userId = userId, type = BetType.RACE.value, leagueId = leagueId))
                raceIds.add(it.id)
            }
        }
        betRepo.saveAll(bets)

        val racesMap = races.associateBy { it.id }
        bets.sortWith(compareBy({ it.type }, { it.race.round }))

        bets.forEach {
            val race = racesMap[it.race.id]
            if (race != null) {
                when (BetType.enumOf(it.type)) {
                    BetType.RACE -> betDTOs.add(
                        BetDTO(
                            it.id,
                            it.type,
                            race.title,
                            race.name,
                            race.flagImgUrl,
                            summarizeBetPoints(it.betItems),
                            getDateRange(race)
                        )
                    )
                    BetType.CHAMPIONSHIP -> betDTOs.add(
                        BetDTO(
                            it.id,
                            it.type,
                            "Championship",
                            "Formula 1 2022 Championship",
                            "",
                            summarizeBetPoints(it.betItems),
                            ""
                        )
                    )
                }

            }
        }
        return betDTOs
    }

    fun getBetsByLeague(userId: Long, leagueId: Long): MutableList<Bet> {
        return betRepo.findAllBetByUserIdAndLeagueId(userId, leagueId)
    }

    fun getBet(betId: Long, username: String): BetDTO {
        val bet = betRepo.findBetById(betId).orElseThrow{ BetNotFoundException("") }
        val race = raceService.getRace(bet.race.id)
        val userId: Long = userService.getUser(username).id
        if (bet.userId != userId) {
            throw AccessForbiddenException()
        }

        return when (BetType.enumOf(bet.type)) {
            BetType.RACE -> BetDTO(
                betId,
                bet.type,
                race.title,
                race.name,
                race.flagImgUrl,
                summarizeBetPoints(bet.betItems),
                getDateRange(race))
            BetType.CHAMPIONSHIP -> BetDTO(
                betId,
                bet.type,
                "Championship",
                "Formula 1 2022 Championship",
                "",
                summarizeBetPoints(bet.betItems),
                getDateRange(race))
        }

    }

    fun getBetItemDTO(betId: Long, betItemType: BetItemType): BetItemDTO {
        val betItem = getBetItem(betId, betItemType)
        val status = evaluateStatus(betItemType, betItem.bet.race)
        val betItemPositions = mutableListOf<PositionDTO>()
        repeat(betItemType.repeatNumber) {
            val positionNum = it + 1
            val position =  positionService.getBetItemPosition(betItem.id, positionNum)

            betItemPositions.add(PositionDTO(position.id, positionNum, betSubjectService.getBetSubject(position.betSubjectId), position.fastestLap))
        }

        val raceId = when(betItemType) {
            BetItemType.CONSTRUCTOR -> 0
            BetItemType.DRIVER -> 0
            else -> betItem.bet.race.id
        }

        return BetItemDTO(betItem.id, betItem.points, betItemType.value, betItemPositions, betId, status.value, raceId)
    }

    fun getBetItemResults(betId: Long, betItemType: BetItemType): BetItemResultDTO {
        val betItem = getBetItem(betId, betItemType)
        val status = evaluateStatus(betItemType, betItem.bet.race)
        if (status == BetItemStatus.OPEN) {
            throw BetItemStillOpenException()
        }
        val result = resultService.getResult(betItem.bet.race.id, betItemType) ?: Result()

        var betItemPositions = positionService.getBetItemPositions(betItem.id)
        var resultPositions = positionService.getResultPositions(result.id)

        if (betItemPositions.size > resultPositions.size) {
            repeat(betItemPositions.size - resultPositions.size) {
                resultPositions = resultPositions.toMutableList()
                (resultPositions as MutableList<Position>).add(Position())
            }
        } else if (betItemPositions.size < resultPositions.size) {
            repeat(resultPositions.size - betItemPositions.size) {
                betItemPositions = betItemPositions.toMutableList()
                (betItemPositions as MutableList<Position>).add(Position())
            }
        }

        return BetItemResultDTO(betItemType.value,
            betItemPositions
                .zip(resultPositions)
                .map { BetItemResultPositionDTO(
                        PositionDTO(0, it.first.position, betSubjectService.getBetSubject(it.first.betSubjectId), it.first.fastestLap),
                        PositionDTO(0, it.second.position, betSubjectService.getBetSubject(it.second.betSubjectId), it.second.fastestLap)
                )})
    }

    private fun getBetItem(betId: Long, betItemType: BetItemType): BetItem {
        return betItemRepo.findBetItemByBetIdAndType(betId, betItemType.value)
            .orElse(
                BetItem(type = betItemType.value,
                    bet = betRepo.findBetById(betId)
                        .orElseThrow { BetNotFoundException("") })
            )
    }

    private fun evaluateStatus(betItemType: BetItemType, race: Race): BetItemStatus {
        var date = betItemType.dateTime.get(race)
        if(betItemType.isChampionshipType()) {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.HOUR, -3)
            date = calendar.time
        }
        if (date.after(Date())) {
            return BetItemStatus.OPEN
        }
        return BetItemStatus.LOCKED
    }

    private fun summarizeBetPoints(betItems: List<BetItem>): Int {
        var points = 0
        betItems.forEach { betItem ->
            points += betItem.points
        }
        return points
    }

    private fun getDateRange(race: Race): String {
        val dateRange = StringBuilder()
        val calendar = Calendar.getInstance()
        calendar.time = race.qualiStartDatetime
        dateRange.append(calendar.get(Calendar.DAY_OF_MONTH)).append(" - ")
        calendar.time = race.raceStartDatetime
        dateRange.append(calendar.get(Calendar.DAY_OF_MONTH)).append(" ")
            .append(SimpleDateFormat("MMM", Locale.ENGLISH).format(calendar.time))
        return dateRange.toString()
    }

    fun saveBetItem(betItemDTO: BetItemDTO): BetItemDTO {
        val bet = betRepo.findBetById(betItemDTO.betId).orElseThrow { BetNotFoundException("") }
        if (evaluateStatus(BetItemType.enumOf(betItemDTO.type), bet.race) != BetItemStatus.OPEN) {
            return BetItemDTO(0, 0, "", mutableListOf(), 0, "", 0)
        }
        val betItem = BetItem(betItemDTO.id, betItemDTO.points, betItemDTO.type, bet)
        val betItemId = betItemRepo.save(betItem).id
        positionService.savePositions(betItemDTO.positions.toPositions(betItemId))

        return getBetItemDTO(betItemDTO.betId, BetItemType.enumOf(betItemDTO.type))
    }
}