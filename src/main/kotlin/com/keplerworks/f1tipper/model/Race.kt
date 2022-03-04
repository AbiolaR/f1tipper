package com.keplerworks.f1tipper.model

import java.util.Date
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Race(
    @Id
    val id: Long,
    val name: String,
    val title: String,
    val flagImgUrl: String,
    val raceStartDatetime: Date,
    val qualiStartDatetime: Date
)
