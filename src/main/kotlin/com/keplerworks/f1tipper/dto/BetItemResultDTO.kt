package com.keplerworks.f1tipper.dto

data class BetItemResultDTO(
    val betItemType: String,
    val betItemResultPositions: List<BetItemResultPositionDTO>
)
