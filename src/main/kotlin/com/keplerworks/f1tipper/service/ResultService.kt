package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.client.ErgastClient
import com.keplerworks.f1tipper.model.Position
import com.keplerworks.f1tipper.model.Race
import com.keplerworks.f1tipper.model.Result
import com.keplerworks.f1tipper.repository.ResultRepository
import com.keplerworks.f1tipper.type.BetType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ResultService @Autowired constructor(
    private val resultRepo: ResultRepository,
    private val raceService: RaceService,
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

        val dnfResult = getResult(race.id, BetType.DNF) ?: Result(raceId = race.id, type = BetType.DNF.value)
        val dnfResultPositions: MutableList<Position> = mutableListOf()


        println(ergastRaceResult.toString())
    }
}