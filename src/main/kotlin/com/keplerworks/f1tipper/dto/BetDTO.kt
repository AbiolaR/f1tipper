package com.keplerworks.f1tipper.dto

data class BetDTO(
    val id: Long,
    val type: String,
    val title: String,
    val name: String,
    val country: String,
    val flagImageUrl: String = "",
    val points: Int,
    val dateRange: String,
    val raceId: Long
)
