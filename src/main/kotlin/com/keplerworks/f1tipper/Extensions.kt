package com.keplerworks.f1tipper

import com.keplerworks.f1tipper.dto.BetDTO
import com.keplerworks.f1tipper.dto.DriverDTO
import com.keplerworks.f1tipper.dto.PositionDTO
import com.keplerworks.f1tipper.model.Bet
import com.keplerworks.f1tipper.model.Driver
import com.keplerworks.f1tipper.model.Position
import com.keplerworks.f1tipper.service.RaceBetItemService

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

fun List<Bet>.toBetDtoList(): List<BetDTO> {
    return this.map { BetDTO(it.id, it.points, it.type, mutableListOf(), it.raceBetItem.id) }
}