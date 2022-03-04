package com.keplerworks.f1tipper.dto

data class PositionDTO(
    val id: Long = 0,
    val position: Int,
    val driver: DriverDTO
)
