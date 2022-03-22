package com.keplerworks.f1tipper.dto

data class BetDTO(
    val id: Long,
    val type: String,
    val title: String,
    val name: String,
    val status: String,
    val country: String,
    val flagImageUrl: String = "",
    val trackSvg: String,
    val points: Int,
    val dateRange: String,
    val raceId: Long
)
