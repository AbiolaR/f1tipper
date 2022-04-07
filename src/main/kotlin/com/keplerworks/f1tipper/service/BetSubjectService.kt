package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.exception.BetSubjectNotFoundException
import com.keplerworks.f1tipper.model.BetSubject
import com.keplerworks.f1tipper.model.Race
import com.keplerworks.f1tipper.repository.BetSubjectRepository
import com.keplerworks.f1tipper.type.BetSubjectType
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class BetSubjectService (private val betSubjectRepo: BetSubjectRepository, private val raceService: RaceService) {
    val logger = KotlinLogging.logger {}

    fun getBetSubject(id: Long): BetSubject {
        if (id == 0L) return BetSubject(BetSubjectType.UNKNOWN)
        return betSubjectRepo.findBetSubjectById(id).orElseThrow {
            logger.error { "Could not find betsubject with id: $id" }
            throw BetSubjectNotFoundException()
        }
    }

    fun getBetSubject(name: String): BetSubject {
        return betSubjectRepo.findBetSubjectByName(name).orElseThrow {
            logger.error { "Could not find betsubject: $name" }
            throw BetSubjectNotFoundException()
        }
    }

    fun getAllBetSubjectsByType(type: BetSubjectType): List<BetSubject> {
        return betSubjectRepo.findAllBetSubjectByType(type)
    }

    fun getBetSubjectsOfTypeOfRace(raceId: Long, type: BetSubjectType): List<BetSubject> {
        val race = raceService.getRace(raceId)
        if (race.round == 0) {
            return getAllBetSubjectsByTypAndNotFlag(type)
        }
        return getAllDriversOfRace(race)
    }

    private fun getAllBetSubjectsByTypAndNotFlag(type: BetSubjectType): List<BetSubject> {
        return betSubjectRepo.findAllBetSubjectByTypeAndFlagNot(type = type)
    }

    private fun getAllDriversOfRace(race: Race): List<BetSubject> {
        val drivers = getAllBetSubjectsByType(BetSubjectType.DRIVER)
        return drivers.filter { driver -> driver.races.contains(race) }
    }

}