package com.keplerworks.f1tipper

import com.keplerworks.f1tipper.dto.BetItemDTO
import com.keplerworks.f1tipper.dto.PositionDTO
import com.keplerworks.f1tipper.model.BetItem
import com.keplerworks.f1tipper.model.Position

fun List<PositionDTO>.toPositions(betItemId: Long): List<Position> {
    val positions: MutableList<Position> = mutableListOf()
    this.forEach {
        positions.add(Position(it.id, it.betSubject.id, it.fastestLap, it.position, betItemId))
        /*when(it.betSubject.type) {
            BetSubjectType.CONSTRUCTOR -> positions.add(Position(it.id, it.betSubject.id, it.fastestLap, it.position, betItemId))
            BetSubjectType.DRIVER -> positions.add(Position(it.id, it.betSubject.id, it.position, betItemId))
        }*/

    }

    return positions
}

fun List<BetItem>.toBetItemDtoList(): List<BetItemDTO> {
    return this.map { BetItemDTO(it.id, it.points, it.type, mutableListOf(), it.bet.id, "") }
}