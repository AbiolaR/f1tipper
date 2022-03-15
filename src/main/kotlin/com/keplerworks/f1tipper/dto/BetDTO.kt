package com.keplerworks.f1tipper.dto

data class BetDTO(
    val id: Long,
    val type: String,
    val title: String,
    val name: String,
    val flagImageUrl: String = "",
    val points: Int
)
