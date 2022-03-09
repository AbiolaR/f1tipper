package com.keplerworks.f1tipper.controller

import com.keplerworks.f1tipper.service.ResultService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/result")
class ResultController @Autowired constructor(private val service: ResultService) {

    @PostMapping("sync/{raceId}")
    fun triggerResultSync(@PathVariable raceId: Long, request: HttpServletRequest): Boolean {
        return service.syncResult(raceId)
    }

}