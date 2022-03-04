package com.keplerworks.f1tipper.repository

import com.keplerworks.f1tipper.model.Position
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface PositionRepository : JpaRepository<Position, Long> {
    fun findAllPositionByBetId(betId: Long): Optional<Position>

    fun findPositionByBetIdAndPosition(betId: Long, position: Int): Optional<Position>
}