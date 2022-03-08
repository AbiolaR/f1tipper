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

    @GetMapping("/{id}")
    fun getStandings(@PathVariable id: Long, request: HttpServletRequest): LeagueStandingsDTO {
        return service.getLeagueStandings(id, request.userPrincipal.name)
    }
}