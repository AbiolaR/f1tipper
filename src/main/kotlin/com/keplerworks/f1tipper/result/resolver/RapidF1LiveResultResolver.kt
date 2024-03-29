package com.keplerworks.f1tipper.result.resolver

import com.keplerworks.f1tipper.client.RapidF1LiveClient
import com.keplerworks.f1tipper.exception.RapidF1LiveSessionNotFound
import com.keplerworks.f1tipper.model.Position
import com.keplerworks.f1tipper.model.Result
import com.keplerworks.f1tipper.model.entity.RapidF1LiveSession
import com.keplerworks.f1tipper.model.rapid.f1live.RapidF1LiveResult
import com.keplerworks.f1tipper.model.rapid.f1live.RapidF1LiveSessionResult
import com.keplerworks.f1tipper.repository.RapidF1LiveSessionRepository
import com.keplerworks.f1tipper.service.BetSubjectService
import com.keplerworks.f1tipper.service.PositionService
import com.keplerworks.f1tipper.service.RaceService
import com.keplerworks.f1tipper.service.ResultService
import com.keplerworks.f1tipper.type.BetItemType
import com.keplerworks.f1tipper.type.RapidF1LiveSessionType
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class RapidF1LiveResultResolver(private val rapidSessionRepo: RapidF1LiveSessionRepository,
                                private val raceService: RaceService,
                                private val resultService: ResultService,
                                private val betSubjectService: BetSubjectService,
                                private val positionService: PositionService) : ResultResolver {

    private val logger = KotlinLogging.logger {  }

    @Value("\${rapidapi.app.f1live.key}")
    lateinit var apiKey: String

    private val client = RapidF1LiveClient.create()

    override fun syncRaceResults(raceId: Long): Boolean {
        try {
            val rapidRaceResult = getRapidSessionResult(raceId, RapidF1LiveSessionType.RACE)
            val rapidFastestLapResult = getRapidSessionResult(raceId, RapidF1LiveSessionType.FASTESTLAP)
            val fastestDriver = rapidFastestLapResult.results.drivers.getOrNull(0)?.name ?: ""

            val raceResult = resultService.getResultOrEmpty(raceId, BetItemType.RACE)
            syncResult(raceResult, rapidRaceResult.generalize(), fastestDriver)

            val dnfResult = resultService.getResultOrEmpty(raceId, BetItemType.DNF)
            syncResult(dnfResult, rapidRaceResult.generalize())
        } catch (exc: Exception) {

            return false
        }
        return true
    }

    override fun syncQualifyingResult(raceId: Long): Boolean {
        try {
            val rapidQualifyingResult = getRapidSessionResult(raceId, RapidF1LiveSessionType.QUALIFYING)
            rapidQualifyingResult.results.drivers =
                rapidQualifyingResult.results.drivers.take(BetItemType.QUALIFYING.repeatNumber)
                        as ArrayList<RapidF1LiveSessionResult.Results.Driver>
            val qualifyingResult = resultService.getResultOrEmpty(raceId, BetItemType.QUALIFYING)
            syncResult(qualifyingResult, rapidQualifyingResult.generalize())
        } catch (exc: Exception) {
            logger.error { exc }
            return false
        }
        return true
    }

    override fun syncChampionshipResult(raceId: Long): Boolean {
        try {
            val rapidConstructorStandingsResult = client.getConstructorStandings(apiKey).get()
            val constructorStandingsResult = resultService.getResultOrEmpty(raceId, BetItemType.CONSTRUCTOR)
            syncResult(constructorStandingsResult, rapidConstructorStandingsResult.generalize())

            val rapidDriverStandingsResult = client.getDriverStandings(apiKey).get()
            val driverStandingsResult = resultService.getResultOrEmpty(raceId, BetItemType.DRIVER)
            syncResult(driverStandingsResult, rapidDriverStandingsResult.generalize())
        } catch (exc: Exception) {
            logger.error { exc }
            return false
        }
        return true
    }

    private fun getRapidSessionResult(raceId: Long, type: RapidF1LiveSessionType): RapidF1LiveSessionResult {
        val rapidSession = rapidSessionRepo.findByRaceIdAndType(raceId, type)
            .orElseThrow {
                logger.error { "No RapidSession found using race id: $raceId" }
                RapidF1LiveSessionNotFound()
            }
        return client.getSession(rapidSession.id, apiKey).get()
    }

    private fun syncResult(result: Result, rapidResult: RapidF1LiveResult, fastestDriver: String = "") {
        val positions = positionService.getResultPositions(result.id)
        val resultPositions: MutableList<Position> = mutableListOf()
        rapidResult.betSubjects.forEach{ rapidBetSubject ->
            val betType = BetItemType.enumOf(result.type)
            if (betType == BetItemType.DNF && rapidBetSubject.retired != 1) return@forEach
            val betSubject = betSubjectService.getBetSubject(rapidBetSubject.name)
            if (betType == BetItemType.DRIVER && betSubject.flag == "R") return@forEach
            resultPositions.add(
                Position(
                    id = positions.find {it.position == rapidBetSubject.position}?.id ?: 0L,
                    betSubjectId = betSubject.id,
                    fastestLap = rapidBetSubject.name == fastestDriver,
                    position = rapidBetSubject.position,
                    result = result
                )
            )
        }
        resultService.saveResult(result)
        positionService.savePositions(resultPositions)
    }


    fun initData() {
        val response = client.getAllRaces(apiKey)
        val rapidRacesResult = response.get()
        val sessions: MutableList<RapidF1LiveSession> = mutableListOf()
        rapidRacesResult.races.forEach { rapidRace ->
            if (rapidRace.name.contains(GRAND_PRIX) && (rapidRace.status == CONFIRMED || rapidRace.status == COMPLETE)) {
                val race = raceService.getRace(rapidRace.name)

                val sessionMap = rapidRace.sessions.associateBy { session ->
                    try {
                        RapidF1LiveSessionType.enumOf(session.sessionName)
                    } catch (exc: Exception) {
                        RapidF1LiveSessionType.NOT_NEEDED
                    }
                }.filter { it.key != RapidF1LiveSessionType.NOT_NEEDED }

                sessionMap.forEach{ session ->
                    sessions.add(RapidF1LiveSession(session.value.id.toLong(), session.key, race.id))
                }

            }
        }
        rapidSessionRepo.deleteAll()
        rapidSessionRepo.saveAll(sessions)
    }

    companion object {
        private const val CONFIRMED = "Confirmed"
        private const val COMPLETE = "Complete"
        private const val GRAND_PRIX = "Grand Prix"
    }

}