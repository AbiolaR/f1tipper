package com.keplerworks.f1tipper.dto

data class BetDTO (
    val id: Long,
    val points: Int,
    val type: String,
    val positions: MutableList<PositionDTO>,
    val raceBetItemId: Long,
    val status: String
)