package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.client.ErgastClient
import com.keplerworks.f1tipper.model.Position
import com.keplerworks.f1tipper.model.Race
import com.keplerworks.f1tipper.model.Result
import com.keplerworks.f1tipper.model.ergast.ErgastDriver
import com.keplerworks.f1tipper.model.ergast.ErgastRaceResult
import com.keplerworks.f1tipper.repository.ResultRepository
import com.keplerworks.f1tipper.type.BetType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ResultService @Autowired constructor(
    private val resultRepo: ResultRepository,
    private val raceService: RaceService,
    private val positionService: PositionService,
    private val driverService: DriverService,
    private val client: ErgastClient = ErgastClient.create()
) {
    fun getResult(raceId: Long, betType: BetType): Result? {
        return resultRepo.findResultByRaceIdAndType(raceId, betType.value).orElse(null)
    }

    fun syncResult(raceId: Long): Boolean {
        val race = raceService.getRace(raceId)
        getRaceResult(race)

        return true
    }


    private fun getRaceResult(race: Race) {
        val response = client.getRaceResult(race.round)
        val ergastRaceResult = response.get()

        val raceResult = getResult(race.id, BetType.RACE) ?: Result(raceId = race.id, type = BetType.RACE.value)
        if (raceResult.id != 0L) {
            positionService.getResultPositions(raceResult.id) as MutableList<Position>
        } else {
            createResultPositions(ergastRaceResult, raceResult)
        }
    }

    private fun createResultPositions(ergastRaceResult: ErgastRaceResult, result: Result) {
        val raceResultPositions: MutableList<Position> = mutableListOf()
        val raceResults = ergastRaceResult.MRData!!.RaceTable?.Races?.get(0)?.Results

            raceResults?.forEach { raceResult ->
                raceResultPositions.add(Position(
                    driverId = findDriverId(raceResult.ErgastDriver!!),
                    position = raceResult.position!!.toInt(),
                    result = result
                    ))
            }
            resultRepo.save(result)
            positionService.savePositions(raceResultPositions)
        try {
        } catch (e: Exception) {
            throw Exception("there was an error saving the results to the DB " + e.message)
        }
    }

    private fun findDriverId(ergastDriver: ErgastDriver): Long {
        return driverService.getDriverByName(ergastDriver.givenName!!, ergastDriver.familyName!!).id
    }
}