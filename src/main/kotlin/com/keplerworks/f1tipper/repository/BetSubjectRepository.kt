package com.keplerworks.f1tipper.repository

import com.keplerworks.f1tipper.model.BetSubject
import com.keplerworks.f1tipper.type.BetSubjectType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BetSubjectRepository : JpaRepository<BetSubject, Long> {
    fun findAllBetSubjectByTypeAndFlagNot(type: BetSubjectType = BetSubjectType.DRIVER, flag: String = "R"): List<BetSubject>

    fun findBetSubjectById(id: Long): Optional<BetSubject>

    fun findBetSubjectByName(name: String): Optional<BetSubject>

    fun findAllBetSubjectByType(type: BetSubjectType): List<BetSubject>
}