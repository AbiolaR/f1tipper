package com.keplerworks.f1tipper.model.rapid.f1live

interface RapidF1LiveBetSubjectResult {

    fun generalize(): RapidF1LiveResult {
        return when (this) {
            is RapidF1LiveSessionResult -> {
                RapidF1LiveResult(this.results.drivers.map { RapidF1LiveResult.BetSubject(it.name, it.position, it.retired, it.gap) })
            }
            is RapidF1LiveConstructorStandingsResult -> {
                RapidF1LiveResult(this.constructors.map { RapidF1LiveResult.BetSubject(it.name, it.position) })
            }
            is RapidF1LiveDriverStandingsResult -> {
                RapidF1LiveResult(this.drivers.map { RapidF1LiveResult.BetSubject(it.name, it.position) })
            }

            else -> throw Exception("Unsupported RapidF1LiveResult implementation")
        }
    }
}