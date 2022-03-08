package com.keplerworks.f1tipper.repository

import com.keplerworks.f1tipper.model.Result
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ResultRepository : JpaRepository<Result, Long> {
    fun findResultByRaceIdAndType(raceId: Long, type: String): Optional<Result>
}