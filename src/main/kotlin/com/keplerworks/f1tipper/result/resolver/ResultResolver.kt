package com.keplerworks.f1tipper.result.resolver

interface ResultResolver {
    fun syncRaceResults(raceId: Long): Boolean
    fun syncQualifyingResult(raceId: Long): Boolean
    fun syncChampionshipResult(raceId: Long): Boolean
}