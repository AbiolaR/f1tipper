package com.keplerworks.f1tipper.repository

import com.keplerworks.f1tipper.model.League
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface LeagueRepository : JpaRepository<League, Long> {
    fun findByName(name: String): Optional<League>
}