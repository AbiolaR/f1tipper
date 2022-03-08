package com.keplerworks.f1tipper.repository

import com.keplerworks.f1tipper.model.Bet
import com.keplerworks.f1tipper.model.Position
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface PositionRepository : JpaRepository<Position, Long> {

    fun findPositionByBetIdAndPosition(betId: Long, position: Int): Optional<Position>

    fun findAllPositionByResultId(resultId: Long): List<Position>

    fun findAllPositionByBetId(betId: Long): List<Position>
}