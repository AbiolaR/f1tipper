package com.keplerworks.f1tipper.controller

import com.keplerworks.f1tipper.service.BetService
import com.keplerworks.f1tipper.service.ResultService
import com.keplerworks.f1tipper.type.BetItemType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/result")
class ResultController @Autowired constructor(private val service: ResultService, private val betService: BetService) {

    @PostMapping("sync/race/{raceId}")
    fun triggerRaceResultSync(@PathVariable raceId: Long, request: HttpServletRequest): Boolean {
        return service.syncRaceResults(raceId)
    }

    @PostMapping("sync/qualifying/{raceId}")
    fun triggerQualifyingResultSync(@PathVariable raceId: Long, request: HttpServletRequest): Boolean {
        return service.syncRaceResults(raceId)
    }

    @GetMapping("{raceId}/{type}/update")
    fun triggerResultSync(@PathVariable raceId: Long, @PathVariable type: String, request: HttpServletRequest): Boolean {
        val betItemType = BetItemType.enumOf(type)
        service.syncResults(raceId, betItemType)
        betService.calculatePoints(raceId, betItemType)
        return true
    }

}