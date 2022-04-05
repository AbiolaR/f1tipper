package com.keplerworks.f1tipper.repository

import com.keplerworks.f1tipper.model.Bet
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BetRepository : JpaRepository<Bet, Long> {
    fun findBetById(id: Long): Optional<Bet>

    fun findBetByRaceIdAndLeagueIdAndUserId(raceId: Long, leagueId: Long, userId: Long): Optional<Bet>

    fun findAllBetByUserId(id: Long): Optional<List<Bet>>

    fun findAllBetByUserIdAndLeagueId(userId: Long, leagueId: Long): MutableList<Bet>

    fun findAllBetByRaceId(raceId: Long): List<Bet>
}