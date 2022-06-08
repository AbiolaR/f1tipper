package com.keplerworks.f1tipper.repository

import com.keplerworks.f1tipper.model.entity.RapidF1LiveSession
import com.keplerworks.f1tipper.type.RapidF1LiveSessionType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RapidF1LiveSessionRepository : JpaRepository<RapidF1LiveSession, Long> {
    fun findByRaceIdAndType(raceId: Long, type: RapidF1LiveSessionType): Optional<RapidF1LiveSession>
}