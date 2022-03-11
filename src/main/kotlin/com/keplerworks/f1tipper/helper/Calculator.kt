package com.keplerworks.f1tipper.helper

import com.keplerworks.f1tipper.model.BetItem
import com.keplerworks.f1tipper.model.Position.Companion.toPositionGroup
import com.keplerworks.f1tipper.service.PositionService
import com.keplerworks.f1tipper.service.ResultService
import com.keplerworks.f1tipper.type.BetType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class Calculator @Autowired constructor(private val resultService: ResultService,
                                        private val positionService: PositionService) {

    fun calculatePoints(raceId: Long, betItem: BetItem): Int {
        var points = 0
        val betType = BetType.enumOf(betItem.type)
        val result = resultService.getResult(raceId, betType) ?: return 0

        val betItemPositions = positionService.getBetItemPositions(betItem.id)
        val resultPositions = positionService.getResultPositions(result.id)

        resultPositions.forEach { resultPosition ->
            val betItemPosition = betItemPositions.firstOrNull {
                resultPosition.position == it.position
            } ?: return@forEach

            if (resultPosition.driverId != betItemPosition.driverId) {
                return@forEach
            }

            if (resultPosition.position == betItemPosition.position) {
                if (resultPosition.position == 1) {
                    points += betType.winPoints
                }
                points += betType.positionPoints
            }

            if (resultPosition.position.toPositionGroup().contains(betItemPosition.position)) {
                points += betType.positionGroupPoints
            }
        }
        return points
    }
}