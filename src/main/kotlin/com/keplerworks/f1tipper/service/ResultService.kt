package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.model.Result
import com.keplerworks.f1tipper.repository.ResultRepository
import com.keplerworks.f1tipper.type.BetType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ResultService @Autowired constructor(private val resultRepo: ResultRepository) {
    fun getResult(raceId: Long, betType: BetType): Result? {
        return resultRepo.findResultByRaceIdAndType(raceId, betType.value).orElse(null)
    }
}