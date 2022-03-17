package com.keplerworks.f1tipper.dto

import com.keplerworks.f1tipper.type.BetSubjectType

data class BetSubjectDTO(
    val id: Long = 0,
    val type: BetSubjectType,
    val name: String,
    val imgUrl: String? = ""
)
