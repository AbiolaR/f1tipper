package com.keplerworks.f1tipper.helper

import com.keplerworks.f1tipper.model.BetItem
import com.keplerworks.f1tipper.model.Position
import com.keplerworks.f1tipper.model.Position.Companion.toPositionGroup
import com.keplerworks.f1tipper.service.PositionService
import com.keplerworks.f1tipper.service.ResultService
import com.keplerworks.f1tipper.type.BetType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class Calculator @Autowired constructor(private val resultService: ResultService,
                                        private val positionService: PositionService) {

    var points = 0
    var betItemPositions: List<Position> = emptyList()
    var resultPositions: List<Position> = emptyList()
    lateinit var betType: BetType

    fun calculatePoints(raceId: Long, betItem: BetItem): Int {
        betType = BetType.enumOf(betItem.type)
        val result = resultService.getResult(raceId, betType) ?: return 0

        betItemPositions = positionService.getBetItemPositions(betItem.id)
        resultPositions = positionService.getResultPositions(result.id)

        return when(betType) {
            BetType.RACE -> calcPerPosition()
            BetType.QUALIFYING -> calcPerPosition()
            BetType.DNF -> calcPerGeneralDriver()
        }
    }

    private fun calcPerPosition(): Int {
        points = 0

        resultPositions.forEach { resultPosition ->
            val betItemPosition = betItemPositions.firstOrNull {
                resultPosition.position == it.position
            } ?: return@forEach

            if (resultPosition.driverId != betItemPosition.driverId) {
                return@forEach
            }

            if (resultPosition.position == betItemPosition.position) {
                points += if (resultPosition.position == 1) {
                    betType.winPoints
                } else {
                    betType.positionPoints
                }
            } else if (resultPosition.position.toPositionGroup().contains(betItemPosition.position)) {
                points += betType.positionGroupPoints
            }
        }

        return points
    }

    private fun calcPerGeneralDriver(): Int {
        points = 0

        val betDrivers = betItemPositions.map { it.driverId }
        val resultDrivers = resultPositions.map { it.driverId }

        betDrivers.forEach { betDriver ->
            if (resultDrivers.contains(betDriver)) {
                points += betType.positionPoints
            }
        }

        return points
    }
}