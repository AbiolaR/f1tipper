package com.keplerworks.f1tipper.repository

import com.keplerworks.f1tipper.model.Race
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface RaceRepository : JpaRepository<Race, Long> {
    fun findRaceById(id: Long): Optional<Race>

}