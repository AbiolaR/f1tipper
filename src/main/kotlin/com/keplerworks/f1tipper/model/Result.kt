package com.keplerworks.f1tipper.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Result (
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    val id: Long = 0,
    val raceId: Long = 0,
    val type: String = ""
)