package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.model.League
import com.keplerworks.f1tipper.repository.LeagueRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LeagueService(@Autowired private val leagueRepo: LeagueRepository) {
    fun getLeague(id: Long): League {
        return leagueRepo.getById(id)
    }
}