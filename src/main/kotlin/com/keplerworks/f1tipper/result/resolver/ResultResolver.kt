package com.keplerworks.f1tipper.result.resolver

import com.keplerworks.f1tipper.type.BetItemTypeGroup

interface ResultResolver {
    fun syncResults(raceId: Long, typeGroup: BetItemTypeGroup): Boolean {
        return when(typeGroup) {
            BetItemTypeGroup.RACE -> syncRaceResults(raceId)
            BetItemTypeGroup.QUALIFYING -> { syncQualifyingResult(raceId) }
            BetItemTypeGroup.CHAMPIONSHIP -> { syncChampionshipResult(raceId) }
        }
    }
    fun syncRaceResults(raceId: Long): Boolean
    fun syncQualifyingResult(raceId: Long): Boolean
    fun syncChampionshipResult(raceId: Long): Boolean
}