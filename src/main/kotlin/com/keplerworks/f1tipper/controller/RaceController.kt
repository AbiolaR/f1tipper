package com.keplerworks.f1tipper.controller

import com.keplerworks.f1tipper.dto.BetDTO
import com.keplerworks.f1tipper.model.BetSubject
import com.keplerworks.f1tipper.service.BetService
import com.keplerworks.f1tipper.service.BetSubjectService
import com.keplerworks.f1tipper.type.BetSubjectType
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/race")
class RaceController (private val betSubjectService: BetSubjectService, private val betService: BetService) {

    @GetMapping("{raceId}/{type}")
    fun getBetSubjectsOfTypeOfRace(@PathVariable raceId: Long, @PathVariable type: BetSubjectType): List<BetSubject> {
        return betSubjectService.getBetSubjectsOfTypeOfRace(raceId, type)
    }

    @GetMapping("/bet")
    fun getBetOfRaceAndLeague(@RequestParam raceId: Long, @RequestParam leagueId: Long, request: HttpServletRequest): BetDTO {
        return betService.getBetByRace(raceId, leagueId, request.userPrincipal.name)
    }
}