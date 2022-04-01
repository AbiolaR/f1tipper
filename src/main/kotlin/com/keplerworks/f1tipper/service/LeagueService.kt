package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.dto.LeagueStandingsDTO
import com.keplerworks.f1tipper.exception.AccessForbiddenException
import com.keplerworks.f1tipper.model.League
import com.keplerworks.f1tipper.repository.LeagueRepository
import com.keplerworks.f1tipper.type.BetItemType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class LeagueService(@Autowired
                    private val leagueRepo: LeagueRepository,
                    private val userService: FormulaUserService,
                    private val betService: BetService) {

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
                if (!BetItemType.enumOf(betItem.type).isChampionshipType()) {
                    totalPoints += betItem.points
                }
            } }
            leagueStandingsDTO.users[it.username] = totalPoints
        }

        leagueStandingsDTO.users.toList().sortedByDescending { it.second }.toMap().toMutableMap()

        return leagueStandingsDTO
    }

    fun getLeagueStatistics(leagueId: Long, username: String): MutableMap<String, String> {
        val statistics: MutableMap<String, String> = mutableMapOf()
        val league = leagueRepo.getById(leagueId)
        val user = userService.getUser(username)

        if (!user.leagues.contains(league)) {
            throw AccessForbiddenException()
        }

        val betItemMap: MutableMap<String, MutableMap<String, Int>> = mutableMapOf()

        enumValues<BetItemType>().forEach { betItemType ->  
            betItemMap[betItemType.value] = mutableMapOf()
        }

        league.users.forEach { formulaUser ->
            val bets = betService.getBetsByLeague(formulaUser.id, leagueId)

            var racePoints = 0
            var dnfPoints = 0
            var qualifyingPoints = 0
            var driverPoints = 0
            var constructorPoints = 0

            bets.forEach { bet ->
                bet.betItems.forEach { betItem ->
                    when (betItem.type) {
                        BetItemType.RACE.value -> {
                            racePoints += betItem.points
                            betItemMap[betItem.type]!![formulaUser.username] = racePoints
                        }
                        BetItemType.DNF.value -> {
                            dnfPoints += betItem.points
                            betItemMap[betItem.type]!![formulaUser.username] = dnfPoints
                        }
                        BetItemType.QUALIFYING.value -> {
                            qualifyingPoints += betItem.points
                            betItemMap[betItem.type]!![formulaUser.username] = qualifyingPoints
                        }
                        BetItemType.DRIVER.value -> {
                            driverPoints += betItem.points
                            betItemMap[betItem.type]!![formulaUser.username] = driverPoints
                        }
                        BetItemType.CONSTRUCTOR.value -> {
                            constructorPoints += betItem.points
                            betItemMap[betItem.type]!![formulaUser.username] = constructorPoints
                        }
                    }
                }
            }
        }

        enumValues<BetItemType>().forEach { betItemType ->
            val stat = betItemMap[betItemType.value]!!.maxByOrNull { it.value }
            statistics[betItemType.value] = if (stat?.value == 0) {
                ""
            } else {
                val equalValues = betItemMap[betItemType.value]!!.filter { it.value == stat?.value  }
                if (equalValues.size > 1) {
                    var names = ""
                    equalValues.forEach { names += "${it.key}, "  }
                    names.dropLast(2)
                } else {
                    stat?.key ?: ""
                }
            }
        }

        return statistics
    }

    @Transactional
    fun joinLeague(leagueName: String, username: String): Boolean {
        val league = leagueRepo.findByName(leagueName).orElse(null) ?: return false
        val user = userService.getUser(username)
        user.leagues.add(league)
        return true
    }


}