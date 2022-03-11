package com.keplerworks.f1tipper.repository

import com.keplerworks.f1tipper.model.Position
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface PositionRepository : JpaRepository<Position, Long> {

    fun findPositionByBetItemIdAndPosition(betId: Long, position: Int): Optional<Position>

    fun findAllPositionByResultId(resultId: Long): List<Position>

    fun findAllPositionByBetItemId(betId: Long): List<Position>
}