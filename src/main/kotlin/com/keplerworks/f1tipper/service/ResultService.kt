package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.client.ErgastClient
import com.keplerworks.f1tipper.model.Position
import com.keplerworks.f1tipper.model.Race
import com.keplerworks.f1tipper.model.Result
import com.keplerworks.f1tipper.model.ergast.ErgastDriver
import com.keplerworks.f1tipper.model.ergast.ErgastResult
import com.keplerworks.f1tipper.model.ergast.QualifyingResults
import com.keplerworks.f1tipper.model.ergast.Results
import com.keplerworks.f1tipper.repository.ResultRepository
import com.keplerworks.f1tipper.type.BetItemType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ResultService @Autowired constructor(
    private val resultRepo: ResultRepository,
    private val raceService: RaceService,
    private val positionService: PositionService,
    private val betSubjectService: BetSubjectService,
    private val client: ErgastClient = ErgastClient.create()
) {
    fun getResult(raceId: Long, betItemType: BetItemType): Result? {
        return resultRepo.findResultByRaceIdAndType(raceId, betItemType.value).orElse(null)
    }

    fun syncRaceResults(raceId: Long): Boolean {
        val race = raceService.getRace(raceId)
        getRaceResult(race)

        return true
    }

    fun syncQualifyingResult(raceId: Long): Boolean {
        val race = raceService.getRace(raceId)
        getQualifyingResult(race)

        return true
    }


    private fun getRaceResult(race: Race) {
        val response = client.getRaceResult(race.round)
        val ergastResult = response.get()
        createResult(ergastResult, race, BetItemType.RACE)
        createResult(ergastResult, race, BetItemType.DNF)
    }

    private fun getQualifyingResult(race: Race) {
        val response = client.getQualifyingResult(race.round)
        val ergastResult = response.get()
        createResult(ergastResult, race, BetItemType.QUALIFYING)
    }

    private fun createResult(ergastResult: ErgastResult, race: Race, betItemType: BetItemType) {
        val raceResult = getResult(race.id, betItemType) ?: Result(raceId = race.id, type =betItemType.value)
        if (raceResult.id != 0L) {
            positionService.getResultPositions(raceResult.id) as MutableList<Position>
        } else {
            createResultPositions(ergastResult, raceResult)
        }
    }

    private fun createResultPositions(ergastResultData: ErgastResult, result: Result) {
        val ergastRace = ergastResultData.MRData!!.RaceTable?.Races?.get(0)

        when(BetItemType.enumOf(result.type)) {
            BetItemType.QUALIFYING -> createQualifyingResultPositions(ergastRace!!.QualifyingResults, result)
            BetItemType.RACE -> createRaceBasedResultPositions(ergastRace!!.Results, result)
            BetItemType.DNF -> createRaceBasedResultPositions(ergastRace!!.Results, result)
            else -> {}
        }
    }

    private fun createRaceBasedResultPositions(ergastResults: ArrayList<Results>, result: Result) {
        val resultPositions: MutableList<Position> = mutableListOf()
        ergastResults.forEach { ergastResult ->
            if (BetItemType.enumOf(result.type) == BetItemType.DNF &&
                (ergastResult.status == "Finished" || ergastResult.status!!.endsWith("Lap"))) {
                return@forEach
            }
            resultPositions.add(Position(
                betSubjectId = findDriverId(ergastResult.ErgastDriver!!),
                position = ergastResult.position!!.toInt(),
                result = result
            ))
        }
        resultRepo.save(result)
        positionService.savePositions(resultPositions)
        try {
        } catch (e: Exception) {
            throw Exception("there was an error saving the results to the DB " + e.message)
        }

    }

    private fun createQualifyingResultPositions(ergastResults: ArrayList<QualifyingResults>, result: Result) {
        val resultPositions: MutableList<Position> = mutableListOf()
        ergastResults.take(BetItemType.enumOf(result.type).repeatNumber).forEach { ergastResult ->
            resultPositions.add(Position(
                betSubjectId = findDriverId(ergastResult.ErgastDriver!!),
                position = ergastResult.position!!.toInt(),
                result = result
            ))
        }
        resultRepo.save(result)
        positionService.savePositions(resultPositions)
        try {
        } catch (e: Exception) {
            throw Exception("there was an error saving the results to the DB " + e.message)
        }
    }

    private fun findDriverId(ergastDriver: ErgastDriver): Long {
        return betSubjectService.getBetSubject("${ergastDriver.givenName!!} ${ergastDriver.familyName!!}").id
    }
}