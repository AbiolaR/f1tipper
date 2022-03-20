package com.keplerworks.f1tipper.controller

import com.keplerworks.f1tipper.result.resolver.ErgastResultResolver
import com.keplerworks.f1tipper.result.resolver.RapidResultResolver
import com.keplerworks.f1tipper.service.BetService
import com.keplerworks.f1tipper.type.BetItemType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/result")
class ResultController (private val betService: BetService,
                        private val ergastResultResolver: ErgastResultResolver,
                        private val rapidResultResolver: RapidResultResolver) {

    @GetMapping("{raceId}/{type}/update")
    fun triggerResultSync(@PathVariable raceId: Long, @PathVariable type: String, request: HttpServletRequest): Boolean {
        val betItemType = BetItemType.enumOf(type)
        if(syncResults(raceId, betItemType)) {
            betService.calculatePoints(raceId, betItemType)
            return true
        }
        return false
    }

    @GetMapping("/rapid/init")
    fun initRapidSessionDataCollection() {
        rapidResultResolver.initData()
    }

    private fun syncResults(raceId: Long, type: BetItemType): Boolean {
        return when(type) {
            BetItemType.RACE -> rapidResultResolver.syncRaceResults(raceId)//ergastResultResolver.syncRaceResults(raceId)
            BetItemType.QUALIFYING -> { false } //ergastResultResolver.syncQualifyingResult(raceId)
            BetItemType.DNF -> { false }
            else -> { false }
        }
    }

}