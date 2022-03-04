package com.keplerworks.f1tipper.controller

import com.keplerworks.f1tipper.dto.BetDTO
import com.keplerworks.f1tipper.dto.RaceBetDTO
import com.keplerworks.f1tipper.dto.RaceBetListItemDTO
import com.keplerworks.f1tipper.exception.AccessForbiddenException
import com.keplerworks.f1tipper.model.*
import com.keplerworks.f1tipper.service.RaceBetItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/racebetitems")
class RaceBetItemController @Autowired constructor(val service: RaceBetItemService) {
    @GetMapping("user/{userId}")
    fun getItemsByUserId(@PathVariable userId: Long): List<RaceBetItem> {
        return service.getRaceBetItemsByUserId(userId)
    }

    @GetMapping("listitem/user/{userId}")
    fun getListItemsByUserId(@PathVariable userId: Long): MutableList<RaceBetListItemDTO> {
        return service.getRaceBetListItemsByUserId(userId)
    }

    @GetMapping("listitem")
    fun getListItemsByAuth(request: HttpServletRequest): MutableList<RaceBetListItemDTO> {
        return service.getRaceBetListItemsByUsername(request.userPrincipal.name)
    }

    @GetMapping("goc/user/{userId}")
    fun getOrCreateItemsByUserId(@PathVariable userId: Long): List<RaceBetItem> {
        return service.getOrCreateRaceBetItemsByUserId(userId)
    }

    @GetMapping("racebet/{id}")
    fun getRaceBetById(request: HttpServletRequest, response: HttpServletResponse, @PathVariable id: Long): RaceBetDTO {
        return service.getRaceBet(id, request.userPrincipal.name)
    }

    @GetMapping("leagueitems/{leagueId}")
    fun getListItemsByAuthAndLeague(request: HttpServletRequest, @PathVariable leagueId: Long): List<RaceBetListItemDTO> {
        return service.getRaceBetItemsByLeague(request.userPrincipal.name, leagueId)
    }

    @GetMapping("qualifying/{id}")
    fun getQualifyingById(@PathVariable id: Long): BetDTO {
        return service.getQualifyingBetPositions(id)
    }

    @GetMapping("race/{id}")
    fun getRaceById(@PathVariable id: Long): BetDTO {
        return service.getRaceBetPositions(id)
    }

    @GetMapping("dnf/{id}")
    fun getDNFById(@PathVariable id: Long): BetDTO {
        return service.getDNFBetPositions(id)
    }

    @PostMapping("save/bet")
    fun saveBet(@RequestBody betDTO: BetDTO): BetDTO {
        return service.saveBet(betDTO)
    }
}