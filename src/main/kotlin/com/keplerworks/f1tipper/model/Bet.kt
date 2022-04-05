package com.keplerworks.f1tipper.model

import com.keplerworks.f1tipper.dto.BetDTO
import com.keplerworks.f1tipper.summarizedPoints
import com.keplerworks.f1tipper.type.BetType
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

) {
    fun toBetDTO(): BetDTO {
        return BetDTO(id, type, race.status(BetType.enumOf(type)).value, betItems.summarizedPoints, race)
    }
}
