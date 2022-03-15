package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.exception.RaceNotFoundException
import com.keplerworks.f1tipper.model.Race
import com.keplerworks.f1tipper.repository.RaceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RaceService @Autowired constructor(private val raceRepo: RaceRepository) {
    fun getRace(raceId: Long): Race {
        return raceRepo.findRaceById(raceId).orElseThrow{RaceNotFoundException("")}
    }

    fun getAllRaces(): List<Race> {
        return raceRepo.findAll().sortedBy { it.round }
    }
}