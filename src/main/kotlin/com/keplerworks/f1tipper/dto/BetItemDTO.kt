package com.keplerworks.f1tipper.dto

data class BetItemDTO (
    val id: Long,
    val points: Int,
    val type: String,
    val positions: MutableList<PositionDTO>,
    val betId: Long,
    val status: String,
    val raceId: Long
)