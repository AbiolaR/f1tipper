package com.keplerworks.f1tipper

import com.keplerworks.f1tipper.dto.PositionDTO
import com.keplerworks.f1tipper.model.BetItem
import com.keplerworks.f1tipper.model.Position

fun List<PositionDTO>.toPositions(betItemId: Long): List<Position> {
    val positions: MutableList<Position> = mutableListOf()
    this.forEach {
        positions.add(Position(it.id, it.betSubject.id, it.fastestLap, it.points, it.position, betItemId))
    }

    return positions
}

val List<BetItem>.summarizedPoints: Int get() {
    var points = 0
    this.forEach { betItem ->
        points += betItem.points
    }
    return points
}