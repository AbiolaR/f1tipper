package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.toPositions
import com.keplerworks.f1tipper.dto.*
import com.keplerworks.f1tipper.exception.AccessForbiddenException
import com.keplerworks.f1tipper.exception.RaceBetItemNotFoundException
import com.keplerworks.f1tipper.model.*
import com.keplerworks.f1tipper.repository.BetItemRepository
import com.keplerworks.f1tipper.repository.RaceBetItemRepository
import com.keplerworks.f1tipper.toBetItemDtoList
import com.keplerworks.f1tipper.type.BetItemStatus
import com.keplerworks.f1tipper.type.BetType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class BetService @Autowired constructor(private val raceBetItemRepo: RaceBetItemRepository,
                                        private val betItemRepo: BetItemRepository,
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
                        it.betItems.toBetItemDtoList()
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

    fun getQualifyingBetPositions(id: Long): BetItemDTO {
        return getBetPositions(id, BetType.QUALIFYING)
    }

    fun getRaceBetPositions(id: Long): BetItemDTO {
        return getBetPositions(id, BetType.RACE)
    }

    fun getDNFBetPositions(id: Long): BetItemDTO {
        return getBetPositions(id, BetType.DNF)
    }

    fun getBetPositions(raceBetItemId: Long, betType: BetType): BetItemDTO {
        val betItem = betItemRepo.findBetByRaceBetItemIdAndType(raceBetItemId, betType.value)
                            .orElse(
                                BetItem(type = betType.value,
                                    raceBetItem = raceBetItemRepo.findRaceBetItemById(raceBetItemId)
                                        .orElse(RaceBetItem(id = raceBetItemId)))
                            )
        val race = raceService.getRace(betItem.raceBetItem.raceId)
        val status = evaluateStatus(betType, race)
        val betItemPositions = mutableListOf<PositionDTO>()
        repeat(betType.repeatNumber) {
            val positionNum = it + 1
            val position =  positionService.getBetItemPosition(betItem.id, positionNum)
            betItemPositions.add(PositionDTO(position.id, positionNum, driverService.getDriver(position.driverId).toDriverDTO()))
        }

        return BetItemDTO(betItem.id, betItem.points, betType.value, betItemPositions, raceBetItemId, status)
    }

    private fun evaluateStatus(betType: BetType, race: Race): String {
        val date = betType.dateTime.get(race)
        if (date.after(Date())) {
            return BetItemStatus.OPEN.value
        }
        return BetItemStatus.LOCKED.value
    }

    fun saveBet(betItemDTO: BetItemDTO): BetItemDTO {
        val raceBetItem = raceBetItemRepo.findRaceBetItemById(betItemDTO.raceBetItemId).orElse(RaceBetItem())
        val betItem = BetItem(betItemDTO.id, betItemDTO.points, betItemDTO.type, raceBetItem)
        val betItemId = betItemRepo.save(betItem).id
        positionService.savePositions(betItemDTO.positions.toPositions(betItemId))

        return getBetPositions(betItemDTO.raceBetItemId, BetType.enumOf(betItemDTO.type))
    }

    /*fun getRaceBetListItemsByUsername(username: String): MutableList<RaceBetListItemDTO> { TODO Remove
        return getRaceBetListItemsByUserId(userService.getUser(username).id)
    }*/

}