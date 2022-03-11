package com.keplerworks.f1tipper.controller

import com.keplerworks.f1tipper.dto.BetItemDTO
import com.keplerworks.f1tipper.dto.RaceBetDTO
import com.keplerworks.f1tipper.dto.RaceBetListItemDTO
import com.keplerworks.f1tipper.service.BetService
import com.keplerworks.f1tipper.type.BetType
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
@RequestMapping("/api/bet")
class RaceBetItemController @Autowired constructor(val service: BetService) {

    @GetMapping("racebet/{id}")
    fun getRaceBetById(request: HttpServletRequest, response: HttpServletResponse, @PathVariable id: Long): RaceBetDTO {
        return service.getRaceBet(id, request.userPrincipal.name)
    }

    @GetMapping("leagueitems/{leagueId}")
    fun getListItemsByAuthAndLeague(request: HttpServletRequest, @PathVariable leagueId: Long): List<RaceBetListItemDTO> {
        return service.getRaceBetItemsByLeague(request.userPrincipal.name, leagueId)
    }

    @GetMapping("item/{type}/{betId}")
    fun getBetItemByTypeAndId(@PathVariable type: String, @PathVariable betId: Long): BetItemDTO {
        return service.getBetPositions(betId, BetType.enumOf(type))
    }

    @PostMapping("item/save")
    fun saveBet(@RequestBody betItemDTO: BetItemDTO): BetItemDTO {
        return service.saveBet(betItemDTO)
    }
}