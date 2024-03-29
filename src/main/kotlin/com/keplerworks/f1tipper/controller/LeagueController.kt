package com.keplerworks.f1tipper.controller

import com.keplerworks.f1tipper.dto.LeagueStandingsDTO
import com.keplerworks.f1tipper.service.LeagueService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/league")
class LeagueController @Autowired constructor(private val service: LeagueService) {

    @GetMapping("/{id}/standings")
    fun getStandings(@PathVariable id: Long, request: HttpServletRequest): LeagueStandingsDTO {
        return service.getLeagueStandings(id, request.userPrincipal.name)
    }

    @GetMapping("/{id}/statistics")
    fun getStatistics(@PathVariable id: Long, request: HttpServletRequest) : MutableMap<String, String> {
        return service.getLeagueStatistics(id, request.userPrincipal.name)
    }

    @GetMapping("/join/{league}")
    fun join(@PathVariable league: String, request: HttpServletRequest): Boolean {
        return service.joinLeague(league, request.userPrincipal.name)
    }
}