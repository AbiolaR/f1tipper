package com.keplerworks.f1tipper.result.resolver

import com.keplerworks.f1tipper.client.RapidClient
import com.keplerworks.f1tipper.model.Position
import com.keplerworks.f1tipper.model.Race
import com.keplerworks.f1tipper.model.Result
import com.keplerworks.f1tipper.model.entity.RapidSession
import com.keplerworks.f1tipper.model.rapid.RapidResult
import com.keplerworks.f1tipper.model.rapid.Session
import com.keplerworks.f1tipper.repository.RapidSessionRepository
import com.keplerworks.f1tipper.service.BetSubjectService
import com.keplerworks.f1tipper.service.PositionService
import com.keplerworks.f1tipper.service.RaceService
import com.keplerworks.f1tipper.service.ResultService
import com.keplerworks.f1tipper.type.BetItemType
import com.keplerworks.f1tipper.type.RapidSessionType
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class RapidResultResolver(private val rapidSessionRepo: RapidSessionRepository,
                          private val raceService: RaceService,
                          private val resultService: ResultService,
                          private val betSubjectService: BetSubjectService,
                          private val positionService: PositionService) : ResultResolver {

    private val logger = KotlinLogging.logger {  }

    @Value("\${rapidapi.app.key}")
    lateinit var apiKey: String

    private val client = RapidClient.create()

    override fun syncRaceResults(raceId: Long): Boolean {
        val race = raceService.getRace(raceId)
        try {
            val rapidRaceResult = getRapidResult(race, RapidSessionType.RACE)
            val rapidFastestLapResult = getRapidResult(race, RapidSessionType.FASTESTLAP)
            val fastestDriver = rapidFastestLapResult.results.drivers.getOrNull(0)?.name ?: ""

            val raceResult = resultService.getResultOrEmpty(raceId, BetItemType.RACE)
            syncResult(raceResult, rapidRaceResult, fastestDriver)

            val dnfResult = resultService.getResultOrEmpty(raceId, BetItemType.DNF)
            syncResult(dnfResult, rapidRaceResult, "")
        } catch (exc: Exception) {
            logger.error { exc }
            return false
        }
        return true
    }




    override fun syncQualifyingResult(raceId: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun syncChampionshipResult(raceId: Long): Boolean {
        TODO("Not yet implemented")
    }

    private fun getRapidResult(race: Race, type: RapidSessionType): RapidResult {
        val rapidSession = rapidSessionRepo.findByRaceIdAndType(race.id, type)
            .orElseThrow { Exception("not found") }
        return client.getSession(rapidSession.id, apiKey).get()
    }

    private fun syncResult(result: Result, rapidRaceResult: RapidResult, fastestDriver: String) {
        val positions = positionService.getResultPositions(result.id)
        val resultPositions: MutableList<Position> = mutableListOf()
        rapidRaceResult.results.drivers.forEach{ driver ->
            if (BetItemType.enumOf(result.type) == BetItemType.DNF && driver.retired != 1) return@forEach
            resultPositions.add(
                Position(
                    id = positions.find {it.position == driver.position}?.id ?: 0L,
                    betSubjectId = betSubjectService.getBetSubject(driver.name).id,
                    fastestLap = driver.name == fastestDriver,
                    position = driver.position,
                    result = result
                )
            )
        }
        resultService.saveResult(result)
        positionService.savePositions(resultPositions)
    }


    fun initData() {
        val response = client.getAllRaces(apiKey)
        val rapidRaces = response.get()
        val sessions: MutableList<RapidSession> = mutableListOf()
        rapidRaces.results.forEach { rapidRace ->
            if (rapidRace.status == "Confirmed" && rapidRace.name.contains("Grand Prix")) {
                val race = raceService.getRace(rapidRace.name)

                val sessionMap : Map<RapidSessionType, Session> = rapidRace.sessions.associateBy { session ->
                    try {
                        RapidSessionType.enumOf(session.sessionName)
                    } catch (exc: Exception) {
                        RapidSessionType.NOTNEEDED
                    }
                }.filter { it.key != RapidSessionType.NOTNEEDED }

                sessionMap.forEach{ session ->
                    sessions.add(RapidSession(session.value.id.toLong(), session.key, race.id))
                }

            }
        }

        rapidSessionRepo.saveAll(sessions)
    }



}