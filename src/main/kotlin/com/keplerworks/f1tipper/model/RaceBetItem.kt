package com.keplerworks.f1tipper.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class RaceBetItem(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val status: String,
    val raceId: Long,
    val userId: Long,
    @ManyToOne
    val league: League,
)
