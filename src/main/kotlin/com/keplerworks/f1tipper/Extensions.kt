package com.keplerworks.f1tipper

import com.keplerworks.f1tipper.dto.DriverDTO
import com.keplerworks.f1tipper.dto.PositionDTO
import com.keplerworks.f1tipper.model.Driver
import com.keplerworks.f1tipper.model.Position

fun List<Driver>.toDriverDTO(): List<DriverDTO> {
    val driverDTOs: MutableList<DriverDTO> = mutableListOf()
    this.forEach{
        driverDTOs.add(it.toDriverDTO())
    }
    return driverDTOs
}

fun List<PositionDTO>.toPositions(betId: Long): List<Position> {
    val positions: MutableList<Position> = mutableListOf()
    this.forEach {
        positions.add(Position(it.id, it.driver.id, it.position, betId))
    }

    return positions
}