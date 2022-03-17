package com.keplerworks.f1tipper.dto

import com.keplerworks.f1tipper.model.BetSubject

data class PositionDTO(
    val id: Long = 0,
    val position: Int,
    val betSubject: BetSubject,
    val fastestLap: Boolean = false
)
