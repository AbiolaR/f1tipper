package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.toPositions
import com.keplerworks.f1tipper.dto.*
import com.keplerworks.f1tipper.exception.AccessForbiddenException
import com.keplerworks.f1tipper.exception.RaceBetItemNotFoundException
import com.keplerworks.f1tipper.exception.RaceNotFoundException
import com.keplerworks.f1tipper.model.*
import com.keplerworks.f1tipper.repository.PositionRepository
import com.keplerworks.f1tipper.repository.BetRepository
import com.keplerworks.f1tipper.repository.RaceBetItemRepository
import com.keplerworks.f1tipper.repository.RaceRepository
import com.keplerworks.f1tipper.type.BetType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RaceBetItemService @Autowired constructor(private val raceBetItemRepo: RaceBetItemRepository,
                                                private val raceRepo: RaceRepository,
                                                private val betRepo: BetRepository,
                                                private val positionRepo: PositionRepository,
                                                private val driverService: DriverService,
                                                private val userService: FormulaUserService,
                                                private val leagueService: LeagueService) {

    fun getRaceBetItemsByLeague(username: String, leagueId: Long): List<RaceBetListItemDTO> {
        val league = leagueService.getLeague(leagueId)
        val userId: Long = userService.getUser(username).id
        val raceBetListItemDTOs: MutableList<RaceBetListItemDTO> = mutableListOf()
        val raceBetItems = raceBetItemRepo.findAllRaceBetItemsByUserIdAndLeagueId(userId, leagueId)
        val raceIds = mutableListOf<Long>()
        raceBetItems.forEach{ raceIds.add(it.raceId) }
        val races: List<Race> = raceRepo.findAll()

        races.forEach {
            if (!raceIds.contains(it.id)) {
                raceBetItems.add(RaceBetItem(raceId = it.id, userId = userId, status = "open", league = league))
                raceIds.add(it.id)
            }
        }
        raceBetItemRepo.saveAll(raceBetItems)

        val racesMap = races.associateBy { it.id }

        raceBetItems.forEach {
            //val race = raceRepo.findRaceById(it.raceId).orElseThrow { RaceNotFoundException("") }
            val race = racesMap[it.raceId]
            if (race != null) {
                raceBetListItemDTOs.add(RaceBetListItemDTO(it.id, race.title, race.flagImgUrl, it.status))
            }
        }
        return raceBetListItemDTOs
    }

    fun getRaceBetItemsByUserId(id: Long): List<RaceBetItem> {
        return raceBetItemRepo.findAllRaceBetItemByUserId(id)
                .orElseThrow {
                    RaceBetItemNotFoundException("RaceBetItem was not found using user id: $id")
                }
    }

    fun getRaceBetListItemsByUserId(id: Long): MutableList<RaceBetListItemDTO> {
        val raceBetListItemDTOs: MutableList<RaceBetListItemDTO> = mutableListOf()

        getRaceBetItemsByUserId(id).forEach{
            val race = raceRepo.findRaceById(it.raceId).orElseThrow { RaceNotFoundException("Race was not found using id: $id") }

            raceBetListItemDTOs.add(RaceBetListItemDTO(it.id, race.title, race.flagImgUrl, it.status))
        }

        return raceBetListItemDTOs
    }

    fun getOrCreateRaceBetItemsByUserId(userId: Long): List<RaceBetItem> {
        val raceIds: List<Long> = raceBetItemRepo.findAllRaceIdsByUserId(userId)
        val races: List<Race> = raceRepo.findAll()
        val newRaceBetItems: MutableList<RaceBetItem> = mutableListOf()

        races.forEach {
            if (!raceIds.contains(it.id)) {
                newRaceBetItems.add(RaceBetItem(raceId = it.id, userId = userId, status = "open", league = League(name="")))
            }
        }

        raceBetItemRepo.saveAll(newRaceBetItems)

        return getRaceBetItemsByUserId(userId)
    }

    fun getRaceBet(id: Long, username: String): RaceBetDTO {
        val raceBetItem = raceBetItemRepo.findRaceBetItemById(id).orElseThrow{ RaceBetItemNotFoundException("") }
        val race = raceRepo.findRaceById(raceBetItem.raceId).orElseThrow { RaceNotFoundException("") }
        val userId: Long = userService.getUser(username).id
        if (raceBetItem.userId != userId) {
            throw AccessForbiddenException()
        }

        return RaceBetDTO(id, race.title, race.name, race.flagImgUrl, raceBetItem.status)
    }

    fun getQualifyingBetPositions(id: Long): BetDTO {
        return getBetPositions(id, BetType.QUALIFYING)
    }

    fun getRaceBetPositions(id: Long): BetDTO {
        return getBetPositions(id, BetType.RACE)
    }

    fun getDNFBetPositions(id: Long): BetDTO {
        return getBetPositions(id, BetType.DNF)
    }

    private fun getBetPositions(raceBetItemId: Long, betType: BetType): BetDTO {
        val bet = betRepo.findBetByRaceBetItemIdAndType(raceBetItemId, betType.value)
                            .orElse(Bet(type = betType.value, raceBetItemId = raceBetItemId))


        //val betPositions = mutableMapOf<PositionDTO, DriverDTO>()
        val betPositions = mutableListOf<PositionDTO>()
        repeat(betType.repeatNumber) {
            val positionNum = it + 1
            val position =  positionRepo.findPositionByBetIdAndPosition(bet.id, positionNum).orElse(Position())
            betPositions.add(PositionDTO(position.id, positionNum, driverService.getDriver(position.driverId).toDriverDTO()))
        //betPositions[PositionDTO(2, positionNum)] = driverService.getDriver(position.driverId).toDriverDTO()
        }

        return BetDTO(bet.id, bet.points, betType.value, betPositions, raceBetItemId)
    }

    fun saveBet(betDTO: BetDTO): BetDTO {
        val bet = Bet(betDTO.id, betDTO.points, betDTO.type, betDTO.raceBetItemId)
        val betId = betRepo.save(bet).id
        positionRepo.saveAll(betDTO.positions.toPositions(betId))

        return getBetPositions(betDTO.raceBetItemId, BetType.enumOf(betDTO.type))
    }

    fun getRaceBetListItemsByUsername(username: String): MutableList<RaceBetListItemDTO> {
        return getRaceBetListItemsByUserId(userService.getUser(username).id)
    }

}