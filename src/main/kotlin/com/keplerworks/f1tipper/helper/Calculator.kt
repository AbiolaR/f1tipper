package com.keplerworks.f1tipper.helper

import com.keplerworks.f1tipper.model.Bet
import com.keplerworks.f1tipper.model.Position.Companion.toPositionGroup
import com.keplerworks.f1tipper.service.PositionService
import com.keplerworks.f1tipper.service.ResultService
import com.keplerworks.f1tipper.type.BetType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class Calculator @Autowired constructor(private val resultService: ResultService,
                                        private val positionService: PositionService) {

    fun calculatePoints(raceId: Long, bet: Bet): Int {
        var points = 0
        val betType = BetType.enumOf(bet.type)
        val result = resultService.getResult(raceId, betType) ?: return 0

        val betPositions = positionService.getBetPositions(bet.id)
        val resultPositions = positionService.getResultPositions(result.id)

        resultPositions.forEach { resultPosition ->
            val betPosition = betPositions.firstOrNull {
                resultPosition.position == it.position
            } ?: return@forEach

            if (resultPosition.driverId != betPosition.driverId) {
                return@forEach
            }

            if (resultPosition.position == betPosition.position) {
                if (resultPosition.position == 1) {
                    points += betType.winPoints
                }
                points += betType.positionPoints
            }

            if (resultPosition.position.toPositionGroup().contains(betPosition.position)) {
                points += betType.positionGroupPoints
            }
        }
        return points
    }
}