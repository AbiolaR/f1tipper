package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.toPositions
import com.keplerworks.f1tipper.dto.*
import com.keplerworks.f1tipper.exception.AccessForbiddenException
import com.keplerworks.f1tipper.exception.BetNotFoundException
import com.keplerworks.f1tipper.model.*
import com.keplerworks.f1tipper.repository.BetItemRepository
import com.keplerworks.f1tipper.repository.BetRepository
import com.keplerworks.f1tipper.type.BetItemStatus
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
        bets.forEach { raceIds.add(it.raceId) }
        val races: List<Race> = raceService.getAllRaces()

        races.forEach {
            if (!raceIds.contains(it.id)) {
                bets.add(Bet(raceId = it.id, userId = userId, status = "open", leagueId = leagueId))
                raceIds.add(it.id)
            }
        }
        betRepo.saveAll(bets)

        val racesMap = races.associateBy { it.id }

        bets.forEach {
            val race = racesMap[it.raceId]
            if (race != null) {
                betDTOs.add(
                    BetDTO(
                        it.id,
                        race.title,
                        race.name,
                        race.flagImgUrl,
                        it.status
                    )
                )
            }
        }
        return betDTOs
    }

    fun getBetsByLeague(userId: Long, leagueId: Long): MutableList<Bet> {
        return betRepo.findAllBetByUserIdAndLeagueId(userId, leagueId)
    }

    fun getBet(betId: Long, username: String): BetDTO {
        val bet = betRepo.findBetById(betId).orElseThrow{ BetNotFoundException("") }
        val race = raceService.getRace(bet.raceId)
        val userId: Long = userService.getUser(username).id
        if (bet.userId != userId) {
            throw AccessForbiddenException()
        }

        return BetDTO(betId, race.title, race.name, race.flagImgUrl, bet.status)
    }

    fun getBetPositions(betId: Long, betType: BetType): BetItemDTO {
        val betItem = betItemRepo.findBetItemByBetIdAndType(betId, betType.value)
                            .orElse(
                                BetItem(type = betType.value,
                                    bet = betRepo.findBetById(betId)
                                        .orElse(Bet(id = betId)))
                            )
        val race = raceService.getRace(betItem.bet.raceId)
        val status = evaluateStatus(betType, race)
        val betItemPositions = mutableListOf<PositionDTO>()
        repeat(betType.repeatNumber) {
            val positionNum = it + 1
            val position =  positionService.getBetItemPosition(betItem.id, positionNum)
            betItemPositions.add(PositionDTO(position.id, positionNum, driverService.getDriver(position.driverId).toDriverDTO()))
        }

        return BetItemDTO(betItem.id, betItem.points, betType.value, betItemPositions, betId, status)
    }

    private fun evaluateStatus(betType: BetType, race: Race): String {
        val date = betType.dateTime.get(race)
        if (date.after(Date())) {
            return BetItemStatus.OPEN.value
        }
        return BetItemStatus.LOCKED.value
    }

    fun saveBet(betItemDTO: BetItemDTO): BetItemDTO {
        val bet = betRepo.findBetById(betItemDTO.betId).orElse(Bet())
        val betItem = BetItem(betItemDTO.id, betItemDTO.points, betItemDTO.type, bet)
        val betItemId = betItemRepo.save(betItem).id
        positionService.savePositions(betItemDTO.positions.toPositions(betItemId))

        return getBetPositions(betItemDTO.betId, BetType.enumOf(betItemDTO.type))
    }
}