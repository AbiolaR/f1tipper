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
                    private val betService: BetService,
                    private val calculator: Calculator) {

    fun getLeague(id: Long): League {
        return leagueRepo.getById(id)
    }

    fun getLeagueStandings(leagueId: Long, username: String): LeagueStandingsDTO {
        val league = leagueRepo.getById(leagueId)
        val user = userService.getUser(username)

        if (!user.leagues.contains(league)) {
            throw AccessForbiddenException()
        }

        val leagueStandingsDTO = LeagueStandingsDTO(league.id, league.name)

        league.users.forEach {
            val bets = betService.getBetsByLeague(it.id, leagueId)

            var totalPoints = 0
            bets.forEach { bet -> bet.betItems.forEach { betItem ->
                totalPoints += betItem.points
            } }
            leagueStandingsDTO.users[totalPoints] = it.username
        }

        leagueStandingsDTO.users.toList().sortedByDescending { it.first }.toMap().toMutableMap()

        return leagueStandingsDTO
    }

    @Transactional
    fun joinLeague(league: String, username: String): Boolean {
        val league = leagueRepo.findByName(league).orElse(null) ?: return false
        val user = userService.getUser(username)
        user.leagues.add(league)
        return true
    }


}