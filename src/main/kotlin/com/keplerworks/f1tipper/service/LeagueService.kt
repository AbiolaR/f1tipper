package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.dto.LeagueStandingsDTO
import com.keplerworks.f1tipper.exception.AccessForbiddenException
import com.keplerworks.f1tipper.helper.Calculator
import com.keplerworks.f1tipper.model.League
import com.keplerworks.f1tipper.repository.LeagueRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class LeagueService(@Autowired
                    private val leagueRepo: LeagueRepository,
                    private val userService: FormulaUserService,
                    private val raceBetItemService: RaceBetItemService,
                    private val calculator: Calculator) {

    fun getLeague(id: Long): League {
        return leagueRepo.getById(id)
    }

    @Transactional
    fun getLeagueStandings(leagueId: Long, username: String): LeagueStandingsDTO {
        val league = leagueRepo.getById(leagueId)
        val user = userService.getUser(username)

        if (!user.leagues.contains(league)) {
            throw AccessForbiddenException()
        }
        val raceBetItems = raceBetItemService.getRaceBetItemsByLeague(user.id, leagueId)
        val leagueStandingsDTO = LeagueStandingsDTO(league.id, league.name)

        league.users.forEach {
            var totalPoints = 0
            raceBetItems.forEach { raceBetItem -> raceBetItem.bets.forEach { bet ->
                if (bet.points == 0) {
                    bet.points = calculator.calculatePoints(raceBetItem.raceId, bet)
                }
                totalPoints += bet.points
            } }
            leagueStandingsDTO.users[it.username] = totalPoints
        }

        return leagueStandingsDTO
    }



}