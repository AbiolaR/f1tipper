package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.dto.BetDTO
import com.keplerworks.f1tipper.dto.BetItemDTO
import com.keplerworks.f1tipper.dto.PositionDTO
import com.keplerworks.f1tipper.exception.AccessForbiddenException
import com.keplerworks.f1tipper.exception.BetNotFoundException
import com.keplerworks.f1tipper.model.Bet
import com.keplerworks.f1tipper.model.BetItem
import com.keplerworks.f1tipper.model.Race
import com.keplerworks.f1tipper.repository.BetItemRepository
import com.keplerworks.f1tipper.repository.BetRepository
import com.keplerworks.f1tipper.toPositions
import com.keplerworks.f1tipper.type.BetItemStatus
import com.keplerworks.f1tipper.type.BetItemType
import com.keplerworks.f1tipper.type.BetType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class BetService @Autowired constructor(private val betRepo: BetRepository,
                                        private val betItemRepo: BetItemRepository,
                                        private val raceService: RaceService,
                                        private val positionService: PositionService,
                                        private val driverService: DriverService,
                                        private val userService: FormulaUserService) {

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
                        )
                    )
                    BetType.CHAMPIONSHIP -> betDTOs.add(
                        BetDTO(
                            it.id,
                            it.type,
                            "Championship",
                            "Formula 1 2022 Championship",
                            "",
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
            BetType.RACE -> BetDTO(betId, bet.type, race.title, race.name, race.flagImgUrl)
            BetType.CHAMPIONSHIP -> BetDTO(betId, bet.type, "Championship", "Formula 1 2022 Championship")
        }

    }

    fun getBetItems(betId: Long, betItemType: BetItemType): BetItemDTO {
        val betItem = betItemRepo.findBetItemByBetIdAndType(betId, betItemType.value)
                            .orElse(
                                BetItem(type = betItemType.value,
                                    bet = betRepo.findBetById(betId)
                                        .orElseThrow { BetNotFoundException("") })
                            )

        val status = evaluateStatus(betItemType, betItem.bet.race)
        val betItemPositions = mutableListOf<PositionDTO>()
        repeat(betItemType.repeatNumber) {
            val positionNum = it + 1
            val position =  positionService.getBetItemPosition(betItem.id, positionNum)
            betItemPositions.add(PositionDTO(position.id, positionNum, driverService.getDriver(position.driverId).toDriverDTO()))
        }

        return BetItemDTO(betItem.id, betItem.points, betItemType.value, betItemPositions, betId, status)
    }

    private fun evaluateStatus(betItemType: BetItemType, race: Race): String {
        val date = betItemType.dateTime.get(race)
        if (date.after(Date())) {
            return BetItemStatus.OPEN.value
        }
        return BetItemStatus.LOCKED.value
    }

    fun saveBetItem(betItemDTO: BetItemDTO): BetItemDTO {
        val bet = betRepo.findBetById(betItemDTO.betId).orElseThrow { BetNotFoundException("") }
        if (evaluateStatus(BetItemType.enumOf(betItemDTO.type), bet.race) != BetItemStatus.OPEN.value) {
            return BetItemDTO(0, 0, "", mutableListOf(), 0, "")
        }
        val betItem = BetItem(betItemDTO.id, betItemDTO.points, betItemDTO.type, bet)
        val betItemId = betItemRepo.save(betItem).id
        positionService.savePositions(betItemDTO.positions.toPositions(betItemId))

        return getBetItems(betItemDTO.betId, BetItemType.enumOf(betItemDTO.type))
    }
}