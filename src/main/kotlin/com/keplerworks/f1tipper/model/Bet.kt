package com.keplerworks.f1tipper.model

import javax.persistence.*

@Entity
class Bet(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val type: String,
    @ManyToOne
    val race: Race,
    val userId: Long = 0,
    val leagueId: Long = 0,
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "bet")
    val betItems: List<BetItem> = emptyList()

)
