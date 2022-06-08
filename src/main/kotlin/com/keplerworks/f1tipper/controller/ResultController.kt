package com.keplerworks.f1tipper.controller

import com.keplerworks.f1tipper.helper.Calculator
import com.keplerworks.f1tipper.result.resolver.ErgastResultResolver
import com.keplerworks.f1tipper.result.resolver.RapidF1LiveResultResolver
import com.keplerworks.f1tipper.type.BetItemTypeGroup
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/result")
class ResultController (private val calculator: Calculator,
                        private val rapidF1LiveResultResolver: RapidF1LiveResultResolver,
                        private val ergastResultResolver: ErgastResultResolver) {

    @GetMapping("{raceId}/{type}/update")
    fun triggerResultSync(@PathVariable raceId: Long, @PathVariable type: BetItemTypeGroup, request: HttpServletRequest): Boolean {
        if(ergastResultResolver.syncResults(raceId, type)) {
            calculator.calculatePoints(raceId, type)
            return true
        }
        return false
    }

    @GetMapping("{raceId}/{type}/calculate")
    fun calculatePoints(@PathVariable raceId: Long, @PathVariable type: BetItemTypeGroup, request: HttpServletRequest): Boolean {
        return try {
            calculator.calculatePoints(raceId, type)
            true
        } catch (e: Exception) {
            false
        }
    }

    @GetMapping("/rapid/init")
    fun initRapidSessionDataCollection() {
        rapidF1LiveResultResolver.initData()
    }


}