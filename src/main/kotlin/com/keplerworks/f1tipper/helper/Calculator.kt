package com.keplerworks.f1tipper.helper

import com.keplerworks.f1tipper.model.BetItem
import com.keplerworks.f1tipper.model.Position
import com.keplerworks.f1tipper.model.Position.Companion.toPositionGroup
import com.keplerworks.f1tipper.service.BetService
import com.keplerworks.f1tipper.service.PositionService
import com.keplerworks.f1tipper.service.ResultService
import com.keplerworks.f1tipper.type.BetItemType
import com.keplerworks.f1tipper.type.BetItemTypeGroup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class Calculator @Autowired constructor(private val betService: BetService,
                                        private val resultService: ResultService,
                                        private val positionService: PositionService) {

    var points = 0
    var betItemPositions: List<Position> = emptyList()
    var resultPositions: List<Position> = emptyList()
    lateinit var betItemType: BetItemType

    fun calculatePoints(raceId: Long, typeGroup: BetItemTypeGroup) {
        when(typeGroup) {
            BetItemTypeGroup.RACE -> {
                calculatePoints(raceId, BetItemType.RACE)
                calculatePoints(raceId, BetItemType.DNF)
            }
            BetItemTypeGroup.QUALIFYING -> {
                calculatePoints(raceId, BetItemType.QUALIFYING)
            }
            BetItemTypeGroup.CHAMPIONSHIP -> {
                calculatePoints(raceId, BetItemType.DRIVER)
                calculatePoints(raceId, BetItemType.CONSTRUCTOR)
            }
        }
    }

    private fun calculatePoints(raceId: Long, type: BetItemType) {
        val betItems = betService.getBetItemsByRace(raceId, type)

        betItems.forEach { betItem ->
            betItem.points =  calculatePoints(raceId, betItem)
        }

        betService.saveAllBetItems(betItems)
    }

    private fun calculatePoints(raceId: Long, betItem: BetItem): Int {
        betItemType = BetItemType.enumOf(betItem.type)
        val result = resultService.getResult(raceId, betItemType) ?: return 0

        betItemPositions = positionService.getBetItemPositions(betItem.id)
        resultPositions = positionService.getResultPositions(result.id)

        return when(betItemType) {
            BetItemType.DNF -> calcPerGeneralDriver()
            else -> calcPerPosition()
        }
    }

    private fun calcPerPosition(): Int {
        points = 0

        resultPositions.forEach { resultPosition ->
            val betItemPosition = betItemPositions.firstOrNull {
                resultPosition.betSubjectId == it.betSubjectId
            } ?: return@forEach

            betItemPosition.points = false

            if(resultPosition.fastestLap && betItemPosition.fastestLap) {
                points += FASTEST_LAP_POINTS
                betItemPosition.points = true
            }

            if (resultPosition.position == betItemPosition.position) {
                points += if (resultPosition.position == 1) {
                    betItemType.winPoints
                } else {
                    betItemType.positionPoints
                }
                betItemPosition.points = true
            } else if (calculateGroup(betItemType)
                        && resultPosition.position.toPositionGroup().contains(betItemPosition.position)) {
                points += betItemType.positionGroupPoints
                betItemPosition.points = true
            }
        }

        return points
    }

    private fun calculateGroup(type: BetItemType): Boolean {
        return when(type) {
            BetItemType.CONSTRUCTOR -> false
            BetItemType.QUALIFYING -> false
            else -> true
        }
    }

    private fun calcPerGeneralDriver(): Int {
        points = 0

        val betDrivers = betItemPositions.map { it.betSubjectId }
        val resultDrivers = resultPositions.map { it.betSubjectId }

        betDrivers.forEach { betDriver ->
            if (resultDrivers.contains(betDriver)) {
                points += betItemType.positionPoints
            }
        }

        return points
    }

    companion object {
        private const val FASTEST_LAP_POINTS = 5
    }
}