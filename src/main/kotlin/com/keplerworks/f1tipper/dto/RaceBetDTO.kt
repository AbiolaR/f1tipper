package com.keplerworks.f1tipper.dto

data class RaceBetDTO(
    val id: Long,
    val title: String,
    val name: String,
    val flagImageUrl: String = "",
    val status: String = "",
    /*val qualifyingId: Long,
    val racePosId: Long,
    val raceDnfId: Long*/
)
