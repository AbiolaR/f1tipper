package com.keplerworks.f1tipper.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Race(
    @Id
    val id: Long,
    val round: Int,
    val name: String,
    val title: String,
    val flagImgUrl: String,
    val trackSvg: String,
    val raceStartDatetime: Date,
    val qualiStartDatetime: Date,
    val country: String
)
