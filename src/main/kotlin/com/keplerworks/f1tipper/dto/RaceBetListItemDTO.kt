package com.keplerworks.f1tipper.dto

data class RaceBetListItemDTO(
    val raceBetId: Long,
    val name: String,
    val flagImageUrl: String?,
    val status: String?,
    val bets: List<BetDTO>
)
