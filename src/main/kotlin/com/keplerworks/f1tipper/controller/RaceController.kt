package com.keplerworks.f1tipper.controller

import com.keplerworks.f1tipper.model.BetSubject
import com.keplerworks.f1tipper.service.BetSubjectService
import com.keplerworks.f1tipper.type.BetSubjectType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/race")
class RaceController (private val betSubjectService: BetSubjectService) {

    @GetMapping("{raceId}/{type}")
    fun getBetSubjectsOfTypeOfRace(@PathVariable raceId: Long, @PathVariable type: BetSubjectType): List<BetSubject> {
        if (raceId == 0L) {
            return betSubjectService.getAllBetSubjectsByTypAndFlag(type)
        }

        return betSubjectService.getAllDriversOfRace(raceId)
    }
}