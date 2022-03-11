package com.keplerworks.f1tipper

import com.keplerworks.f1tipper.dto.BetItemDTO
import com.keplerworks.f1tipper.dto.DriverDTO
import com.keplerworks.f1tipper.dto.PositionDTO
import com.keplerworks.f1tipper.model.BetItem
import com.keplerworks.f1tipper.model.Driver
import com.keplerworks.f1tipper.model.Position

fun List<Driver>.toDriverDTO(): List<DriverDTO> {
    val driverDTOs: MutableList<DriverDTO> = mutableListOf()
    this.forEach{
        driverDTOs.add(it.toDriverDTO())
    }
    return driverDTOs
}

fun List<PositionDTO>.toPositions(betItemId: Long): List<Position> {
    val positions: MutableList<Position> = mutableListOf()
    this.forEach {
        positions.add(Position(it.id, it.driver.id, it.position, betItemId))
    }

    return positions
}

fun List<BetItem>.toBetItemDtoList(): List<BetItemDTO> {
    return this.map { BetItemDTO(it.id, it.points, it.type, mutableListOf(), it.raceBetItem.id, "") }
}