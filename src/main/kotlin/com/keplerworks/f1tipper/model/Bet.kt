package com.keplerworks.f1tipper.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
data class Bet(
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    val id: Long = 0,
    var points: Int = 0,
    val type: String,
    @JsonIgnore
    @ManyToOne
    val raceBetItem: RaceBetItem
)
