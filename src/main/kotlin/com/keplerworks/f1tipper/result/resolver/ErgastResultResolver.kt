package com.keplerworks.f1tipper.result.resolver

import com.keplerworks.f1tipper.client.ErgastClient
import com.keplerworks.f1tipper.model.BetSubject
import com.keplerworks.f1tipper.model.Position
import com.keplerworks.f1tipper.model.Result
import com.keplerworks.f1tipper.model.ergast.ErgastResult
import com.keplerworks.f1tipper.model.ergast.ErgastResult.ErgastBetSubject
import com.keplerworks.f1tipper.service.BetSubjectService
import com.keplerworks.f1tipper.service.PositionService
import com.keplerworks.f1tipper.service.RaceService
import com.keplerworks.f1tipper.service.ResultService
import com.keplerworks.f1tipper.type.BetItemType
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class ErgastResultResolver (private val betSubjectService: BetSubjectService,
                            private val positionService: PositionService,
                            private val raceService: RaceService,
                            private val resultService: ResultService) : ResultResolver {

    private val logger = KotlinLogging.logger {  }
    private val client: ErgastClient = ErgastClient.create()

    override fun syncRaceResults(raceId: Long): Boolean {
        try {
            val race = raceService.getRace(raceId)
            val ergastRaceResponse = client.getRaceResult(race.round).get()
            val ergastRaceResult = ergastRaceResponse.mRData.raceTable.race.first().raceResults

            val raceResult = resultService.getResultOrEmpty(raceId, BetItemType.RACE)
            syncResult(raceResult, ergastRaceResult)

            val dnfResult = resultService.getResultOrEmpty(raceId, BetItemType.DNF)
            syncResult(dnfResult, ergastRaceResult)
        } catch (exc: Exception) {
            logger.error { exc }
            return false
        }
        return true
    }

    override fun syncQualifyingResult(raceId: Long): Boolean {
        try {
            val race = raceService.getRace(raceId)
            val ergastSprintResponse = client.getSprintRaceResult(race.round).get()
            val qualifyingResult = resultService.getResultOrEmpty(raceId, BetItemType.QUALIFYING)

            if (ergastSprintResponse.mRData.raceTable.race.size == 0) {
                val ergastQualifyingResponse = client.getQualifyingResult(race.round).get()
                val ergastQualifyingResult = ergastQualifyingResponse.mRData.raceTable.race.first().qualifyingResults
                    .take(BetItemType.QUALIFYING.repeatNumber)
                        as ArrayList<ErgastResult>

                syncResult(qualifyingResult, ergastQualifyingResult)
            } else {
                val ergastSprintResult = ergastSprintResponse.mRData.raceTable.race.first().sprintResults
                    .take(BetItemType.QUALIFYING.repeatNumber)
                        as ArrayList<ErgastResult>
                syncResult(qualifyingResult, ergastSprintResult)
            }

        } catch (exc: Exception) {
            logger.error { exc }
            return false
        }
        return true
    }

    override fun syncChampionshipResult(raceId: Long): Boolean {
        try {
            val ergastDriverResponse = client.getDriverStandings().get()
            val driverResult = resultService.getResultOrEmpty(raceId, BetItemType.DRIVER)
            syncResult(driverResult, ergastDriverResponse.mRData.standingsTable.standingsLists.first().driverStandings)

            val ergastConstructorResponse = client.getConstructorStandings().get()
            val constructorResult = resultService.getResultOrEmpty(raceId, BetItemType.CONSTRUCTOR)
            syncResult(constructorResult, ergastConstructorResponse.mRData.standingsTable.standingsLists.first().constructorStandings)
        } catch (exc: Exception) {
            logger.error { exc }
            return false
        }
        return true
    }

    private fun syncResult(result: Result, ergastResults: ArrayList<ErgastResult>) {
        val positions = positionService.getResultPositions(result.id)
        val resultPositions: MutableList<Position> = mutableListOf()

        ergastResults.forEach { ergastResult ->
            val betType = BetItemType.enumOf(result.type)
            val betSubject = findBetSubjectByNameOrAlias(ergastResult.driver ?: ergastResult.constructor!!)

            if (betType == BetItemType.DNF && ergastResult.positionText != "R") return@forEach
            if (betType == BetItemType.DRIVER && betSubject.flag == "R") return@forEach

            resultPositions.add(
                Position(
                    id = positions.find { it.position == ergastResult.position.toInt() }?.id ?: 0L,
                    betSubjectId = betSubject.id,
                    fastestLap = ergastResult.fastestLap.rank.toInt() == 1,
                    position = ergastResult.position.toInt(),
                    result = result
                )
            )
        }
        resultService.saveResult(result)
        positionService.savePositions(resultPositions)
    }

    private fun findBetSubjectByNameOrAlias(ergastBetSubject: ErgastBetSubject): BetSubject {
        val name = ergastBetSubject.name ?: "${ergastBetSubject.givenName} ${ergastBetSubject.familyName}"
        return try {
            betSubjectService.getBetSubject(name)
        } catch (exc: Exception) {
            betSubjectService.getBetSubjectByAlias(name)
        }
    }

}