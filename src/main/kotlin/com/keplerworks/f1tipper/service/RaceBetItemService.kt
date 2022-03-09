package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.toPositions
import com.keplerworks.f1tipper.dto.*
import com.keplerworks.f1tipper.exception.AccessForbiddenException
import com.keplerworks.f1tipper.exception.RaceBetItemNotFoundException
import com.keplerworks.f1tipper.exception.RaceNotFoundException
import com.keplerworks.f1tipper.model.*
import com.keplerworks.f1tipper.repository.BetRepository
import com.keplerworks.f1tipper.repository.RaceBetItemRepository
import com.keplerworks.f1tipper.repository.RaceRepository
import com.keplerworks.f1tipper.toBetDtoList
import com.keplerworks.f1tipper.type.BetStatus
import com.keplerworks.f1tipper.type.BetType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class RaceBetItemService @Autowired constructor(private val raceBetItemRepo: RaceBetItemRepository,
                                                private val betRepo: BetRepository,
                                                private val raceService: RaceService,
                                                private val positionService: PositionService,
                                                private val driverService: DriverService,
                                                private val userService: FormulaUserService) {

    fun getRaceBetItemsByLeague(username: String, leagueId: Long): List<RaceBetListItemDTO> {
        val userId: Long = userService.getUser(username).id
        val raceBetListItemDTOs: MutableList<RaceBetListItemDTO> = mutableListOf()
        val raceBetItems = getRaceBetItemsByLeague(userId, leagueId)
        val raceIds = mutableListOf<Long>()
        raceBetItems.forEach { raceIds.add(it.raceId) }
        val races: List<Race> = raceService.getAllRaces()

        races.forEach {
            if (!raceIds.contains(it.id)) {
                raceBetItems.add(RaceBetItem(raceId = it.id, userId = userId, status = "open", leagueId = leagueId))
                raceIds.add(it.id)
            }
        }
        raceBetItemRepo.saveAll(raceBetItems)

        val racesMap = races.associateBy { it.id }

        raceBetItems.forEach {
            //val race = raceRepo.findRaceById(it.raceId).orElseThrow { RaceNotFoundException("") }
            val race = racesMap[it.raceId]
            if (race != null) {
                raceBetListItemDTOs.add(
                    RaceBetListItemDTO(
                        it.id,
                        race.title,
                        race.flagImgUrl,
                        it.status,
                        it.bets.toBetDtoList()
                    )
                )
            }
        }
        return raceBetListItemDTOs
    }

    fun getRaceBetItemsByLeague(userId: Long, leagueId: Long): MutableList<RaceBetItem> {
        return raceBetItemRepo.findAllRaceBetItemsByUserIdAndLeagueId(userId, leagueId)
    }

    /*fun getRaceBetItemsByUserId(id: Long): List<RaceBetItem> { TODO Remove
        return raceBetItemRepo.findAllRaceBetItemByUserId(id)
                .orElseThrow {
                    RaceBetItemNotFoundException("RaceBetItem was not found using user id: $id")
                }
    }*/

    /*fun getRaceBetListItemsByUserId(id: Long): MutableList<RaceBetListItemDTO> { TODO Remove
        val raceBetListItemDTOs: MutableList<RaceBetListItemDTO> = mutableListOf()

        getRaceBetItemsByUserId(id).forEach{
            val race = raceRepo.findRaceById(it.raceId).orElseThrow { RaceNotFoundException("Race was not found using id: $id") }

            raceBetListItemDTOs.add(RaceBetListItemDTO(it.id, race.title, race.flagImgUrl, it.status))
        }

        return raceBetListItemDTOs
    }*/

    /*fun getOrCreateRaceBetItemsByUserId(userId: Long): List<RaceBetItem> { TODO Remove
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
    }*/

    fun getRaceBet(raceBetItemId: Long, username: String): RaceBetDTO {
        val raceBetItem = raceBetItemRepo.findRaceBetItemById(raceBetItemId).orElseThrow{ RaceBetItemNotFoundException("") }
        val race = raceService.getRace(raceBetItem.raceId)
        val userId: Long = userService.getUser(username).id
        if (raceBetItem.userId != userId) {
            throw AccessForbiddenException()
        }

        return RaceBetDTO(raceBetItemId, race.title, race.name, race.flagImgUrl, raceBetItem.status)
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
                            .orElse(
                                Bet(type = betType.value,
                                    raceBetItem = raceBetItemRepo.findRaceBetItemById(raceBetItemId)
                                        .orElse(RaceBetItem(id = raceBetItemId)))
                            )//Bet(type = betType.value, raceBetItemId = raceBetItemId))
        val race = raceService.getRace(bet.raceBetItem.raceId)
        val status = evaluateStatus(betType, race)
        //val betPositions = mutableMapOf<PositionDTO, DriverDTO>()
        val betPositions = mutableListOf<PositionDTO>()
        repeat(betType.repeatNumber) {
            val positionNum = it + 1
            val position =  positionService.getBetPosition(bet.id, positionNum)
            betPositions.add(PositionDTO(position.id, positionNum, driverService.getDriver(position.driverId).toDriverDTO()))
        //betPositions[PositionDTO(2, positionNum)] = driverService.getDriver(position.driverId).toDriverDTO()
        }

        return BetDTO(bet.id, bet.points, betType.value, betPositions, raceBetItemId, status)
    }

    private fun evaluateStatus(betType: BetType, race: Race): String {
        val date = betType.dateTime.get(race)
        if (date.after(Date())) {
            return BetStatus.OPEN.value
        }
        return BetStatus.LOCKED.value
    }

    fun saveBet(betDTO: BetDTO): BetDTO {
        val raceBetItem = raceBetItemRepo.findRaceBetItemById(betDTO.raceBetItemId).orElse(RaceBetItem())
        val bet = Bet(betDTO.id, betDTO.points, betDTO.type, raceBetItem)
        val betId = betRepo.save(bet).id
        positionService.savePositions(betDTO.positions.toPositions(betId))

        return getBetPositions(betDTO.raceBetItemId, BetType.enumOf(betDTO.type))
    }

    /*fun getRaceBetListItemsByUsername(username: String): MutableList<RaceBetListItemDTO> { TODO Remove
        return getRaceBetListItemsByUserId(userService.getUser(username).id)
    }*/

}