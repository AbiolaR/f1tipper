package com.keplerworks.f1tipper.model

import javax.persistence.*

@Entity
class RaceBetItem(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val status: String = "", //TODO Remove
    val raceId: Long = 0,
    val userId: Long = 0,
    //@ManyToOne
    //val league: League = League(name = ""),
    val leagueId: Long = 0,
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "raceBetItem")
    val bets: List<Bet> = emptyList()

)
