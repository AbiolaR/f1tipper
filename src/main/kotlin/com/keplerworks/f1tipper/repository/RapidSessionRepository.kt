package com.keplerworks.f1tipper.repository

import com.keplerworks.f1tipper.model.entity.RapidSession
import com.keplerworks.f1tipper.type.RapidSessionType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RapidSessionRepository : JpaRepository<RapidSession, Long> {
    fun findByRaceIdAndType(raceId: Long, type: RapidSessionType): Optional<RapidSession>
}