package com.keplerworks.f1tipper.controller

import com.keplerworks.f1tipper.dto.BetDTO
import com.keplerworks.f1tipper.dto.BetItemDTO
import com.keplerworks.f1tipper.dto.BetItemResultDTO
import com.keplerworks.f1tipper.service.BetService
import com.keplerworks.f1tipper.type.BetItemType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/bet")
class BetController @Autowired constructor(val service: BetService) {

    @GetMapping("{id}")
    fun getBetById(request: HttpServletRequest, response: HttpServletResponse, @PathVariable id: Long): BetDTO {
        return service.getBet(id, request.userPrincipal.name)
    }

    @GetMapping("all/{leagueId}")
    fun getBetsByAuthAndLeague(request: HttpServletRequest, @PathVariable leagueId: Long): List<BetDTO> {
        return service.getBetsByLeague(request.userPrincipal.name, leagueId)
    }

    @GetMapping("item/{type}/{betId}")
    fun getBetItemByTypeAndId(@PathVariable type: String, @PathVariable betId: Long): BetItemDTO {
        return service.getBetItemDTO(betId, BetItemType.enumOf(type))
    }

    @GetMapping("item/{type}/{betId}/result")
    fun getBetItemResultByTypeAndId(@PathVariable type: String, @PathVariable betId: Long): BetItemResultDTO {
        return service.getBetItemResults(betId, BetItemType.enumOf(type))
    }

    @PostMapping("item/save")
    fun saveBet(@RequestBody betItemDTO: BetItemDTO): BetItemDTO {
        return service.saveBetItem(betItemDTO)
    }
}