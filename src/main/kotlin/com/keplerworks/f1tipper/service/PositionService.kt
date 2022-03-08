package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.model.Position
import com.keplerworks.f1tipper.repository.PositionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PositionService @Autowired constructor(private val positionRepo: PositionRepository) {

    fun getBetPosition(betId: Long, positionNumber: Int): Position {
        return positionRepo.findPositionByBetIdAndPosition(betId, positionNumber).orElse(Position())
    }

    fun getBetPositions(betId: Long): List<Position> {
        return positionRepo.findAllPositionByBetId(betId).sortedBy { it.position }
    }

    fun getResultPositions(resultId: Long): List<Position> {
        return positionRepo.findAllPositionByResultId(resultId).sortedBy { it.position }
    }

    fun savePositions(positions: List<Position>) {
        positionRepo.saveAll(positions)
    }
}