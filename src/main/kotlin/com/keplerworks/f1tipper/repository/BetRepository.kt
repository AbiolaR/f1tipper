package com.keplerworks.f1tipper.repository

import com.keplerworks.f1tipper.model.Bet
import com.keplerworks.f1tipper.model.RaceBetItem
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BetRepository : JpaRepository<Bet, Long> {
    fun findBetByRaceBetItemIdAndType(raceBetItemId: Long, type: String): Optional<Bet>
}