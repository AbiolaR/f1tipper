package com.keplerworks.f1tipper.service

import com.keplerworks.f1tipper.model.Result
import com.keplerworks.f1tipper.repository.ResultRepository
import com.keplerworks.f1tipper.type.BetItemType
import org.springframework.stereotype.Service

@Service
class ResultService (private val resultRepo: ResultRepository) {
    fun getResult(raceId: Long, betItemType: BetItemType): Result? {
        return resultRepo.findResultByRaceIdAndType(raceId, betItemType.value).orElse(null)
    }

    fun getResultOrEmpty(raceId: Long, betItemType: BetItemType): Result {
        return resultRepo.findResultByRaceIdAndType(raceId, betItemType.value)
            .orElse(Result(raceId = raceId, type = betItemType.value))
    }



    fun saveResult(result: Result) {
        resultRepo.save(result)
    }
}