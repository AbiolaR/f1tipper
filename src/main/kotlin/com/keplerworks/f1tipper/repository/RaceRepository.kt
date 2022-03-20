package com.keplerworks.f1tipper.repository

import com.keplerworks.f1tipper.model.Race
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RaceRepository : JpaRepository<Race, Long> {
    fun findRaceById(id: Long): Optional<Race>

    fun findRaceByTitle(title: String): Optional<Race>
}