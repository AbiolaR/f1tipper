package com.keplerworks.f1tipper.dto

import com.keplerworks.f1tipper.model.Race

data class BetDTO(
    val id: Long,
    val type: String,
    val status: String,
    val points: Int,
    val race: Race
)
