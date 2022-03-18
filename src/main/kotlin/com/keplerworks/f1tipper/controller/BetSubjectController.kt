package com.keplerworks.f1tipper.controller

import com.keplerworks.f1tipper.model.BetSubject
import com.keplerworks.f1tipper.service.BetSubjectService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/betsubject")
class BetSubjectController(private val service: BetSubjectService) {

    /*@GetMapping("{type}")
    fun getAllBetSubjectsOfType(@PathVariable type: BetSubjectType): List<BetSubject> {
        return service.getAllBetSubjectsByType(type)
    }*/

    @GetMapping("id/{id}")
    fun getBetSubjectById(@PathVariable id: Long): BetSubject {
        return service.getBetSubject(id)
    }
}