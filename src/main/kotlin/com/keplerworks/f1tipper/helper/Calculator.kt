package com.keplerworks.f1tipper.helper

import com.keplerworks.f1tipper.model.BetItem
import com.keplerworks.f1tipper.model.Position
import com.keplerworks.f1tipper.model.Position.Companion.toPositionGroup
import com.keplerworks.f1tipper.service.PositionService
import com.keplerworks.f1tipper.service.ResultService
import com.keplerworks.f1tipper.type.BetItemType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class Calculator @Autowired constructor(private val resultService: ResultService,
                                        private val positionService: PositionService) {

    var points = 0
    var betItemPositions: List<Position> = emptyList()
    var resultPositions: List<Position> = emptyList()
    lateinit var betItemType: BetItemType

    fun calculatePoints(raceId: Long, betItem: BetItem): Int {
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
                resultPosition.position == it.position
            } ?: return@forEach

            if (resultPosition.betSubjectId != betItemPosition.betSubjectId) {
                return@forEach
            }

            if (resultPosition.position == betItemPosition.position) {
                points += if (resultPosition.position == 1) {
                    betItemType.winPoints
                } else {
                    betItemType.positionPoints
                }
            } else if (resultPosition.position.toPositionGroup().contains(betItemPosition.position)) {
                points += betItemType.positionGroupPoints
            }
        }

        return points
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
}