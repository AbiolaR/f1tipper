package com.keplerworks.f1tipper.repository

import com.keplerworks.f1tipper.model.BetItem
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BetItemRepository : JpaRepository<BetItem, Long> {
    fun findBetByRaceBetItemIdAndType(raceBetItemId: Long, type: String): Optional<BetItem>
}