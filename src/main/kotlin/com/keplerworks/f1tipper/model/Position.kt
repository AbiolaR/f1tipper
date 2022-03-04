package com.keplerworks.f1tipper.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Position (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val driverId: Long = 0,
    val position: Int = 0,
    val betId: Long = 0
)