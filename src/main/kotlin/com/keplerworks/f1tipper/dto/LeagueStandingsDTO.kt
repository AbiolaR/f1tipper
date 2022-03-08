package com.keplerworks.f1tipper.dto

data class LeagueStandingsDTO (
    val id: Long,
    val name: String,
    val users: MutableMap<String, Int> = mutableMapOf()
)
