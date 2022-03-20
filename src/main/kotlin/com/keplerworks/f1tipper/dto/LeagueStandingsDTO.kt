package com.keplerworks.f1tipper.dto

data class LeagueStandingsDTO (
    val id: Long,
    val name: String,
    var users: MutableMap<Int, String> = mutableMapOf()
)
