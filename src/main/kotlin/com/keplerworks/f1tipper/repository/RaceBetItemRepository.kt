package com.keplerworks.f1tipper.repository

import com.keplerworks.f1tipper.model.Race
import com.keplerworks.f1tipper.model.RaceBetItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface RaceBetItemRepository : JpaRepository<RaceBetItem, Long> {
    fun findRaceBetItemById(id: Long): Optional<RaceBetItem>

    fun findAllRaceBetItemByUserId(id: Long): Optional<List<RaceBetItem>>

    fun findAllRaceBetItemsByUserIdAndLeagueId(userId: Long, leagueId: Long): MutableList<RaceBetItem>

    @Query(value = "SELECT raceId FROM RaceBetItem WHERE userId = ?1")
    fun findAllRaceIdsByUserId(id: Long): List<Long>
}