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

@Service
class BetService @Autowired constructor(private val betRepo: BetRepository,
                                        private val betItemRepo: BetItemRepository,
                                        private val raceService: RaceService,
                                        private val positionService: PositionService,
                                        private val betSubjectService: BetSubjectService,
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
            bets.add(Bet(race = races.firstOrNull { race -> race.round == 0 }!!, userId = userId,
                            type = BetType.CHAMPIONSHIP.value, leagueId = leagueId))
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

        bets.forEach { bet ->
            val race = racesMap[bet.race.id]
            if (race != null) {
                betDTOs.add(bet.toBetDTO())
            }
        }
        return betDTOs
    }

    fun getBetsByLeague(userId: Long, leagueId: Long): MutableList<Bet> {
        return betRepo.findAllBetByUserIdAndLeagueId(userId, leagueId)
    }

    fun getBetItemsByRace(raceId: Long, type: BetItemType): List<BetItem> {
        val bets = betRepo.findAllBetByRaceId(raceId)
        return bets.flatMap { bet -> bet.betItems.filter { betItem -> betItem.type == type.value } }
    }

    fun getBetByRace(raceId: Long, leagueId: Long, username: String): BetDTO {
        val userId: Long = userService.getUser(username).id
        val bet = betRepo.findBetByRaceIdAndLeagueIdAndUserId(raceId, leagueId, userId)
            .orElseThrow{ BetNotFoundException("") }
        return getBet(bet, userId)
    }

    fun getBet(betId: Long, username: String): BetDTO {
        val bet = betRepo.findBetById(betId).orElseThrow{ BetNotFoundException("") }
        val userId: Long = userService.getUser(username).id
        return getBet(bet, userId)
    }

    fun getBet(bet: Bet, userId: Long): BetDTO {
        if (bet.userId != userId) {
            throw AccessForbiddenException()
        }
        return bet.toBetDTO()
    }

    fun getBetItemDTO(betId: Long, betItemType: BetItemType): BetItemDTO {
        val betItem = getBetItem(betId, betItemType)
        val status = betItem.bet.race.status(betItemType)
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
        val status = betItem.bet.race.status(betItemType)
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
                        PositionDTO(0, it.first.position, betSubjectService.getBetSubject(it.first.betSubjectId), it.first.fastestLap, it.first.points),
                        PositionDTO(0, it.second.position, betSubjectService.getBetSubject(it.second.betSubjectId), it.second.fastestLap, it.second.points)
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

    fun saveBetItem(betItemDTO: BetItemDTO): BetItemDTO {
        val bet = betRepo.findBetById(betItemDTO.betId).orElseThrow { BetNotFoundException("") }
        if (bet.race.status(BetItemType.enumOf(betItemDTO.type)) != BetItemStatus.OPEN) {
            return BetItemDTO(0, 0, "", mutableListOf(), 0, "", 0)
        }
        val betItem = BetItem(betItemDTO.id, betItemDTO.points, betItemDTO.type, bet)
        val betItemId = betItemRepo.save(betItem).id
        positionService.savePositions(betItemDTO.positions.toPositions(betItemId))

        return getBetItemDTO(betItemDTO.betId, BetItemType.enumOf(betItemDTO.type))
    }

    fun saveAllBetItems(betItems: List<BetItem>) {
        betItemRepo.saveAll(betItems)
    }
}