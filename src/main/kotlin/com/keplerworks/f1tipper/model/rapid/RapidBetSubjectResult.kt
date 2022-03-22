package com.keplerworks.f1tipper.model.rapid

interface RapidBetSubjectResult {

    fun generalize(): RapidResult {
        return when (this) {
            is RapidSessionResult -> {
                RapidResult(this.results.drivers.map { RapidResult.BetSubject(it.name, it.position, it.retired) })
            }
            is RapidConstructorStandingsResult -> {
                RapidResult(this.constructors.map { RapidResult.BetSubject(it.name, it.position) })
            }
            is RapidDriverStandingsResult -> {
                RapidResult(this.drivers.map { RapidResult.BetSubject(it.name, it.position) })
            }

            else -> throw Exception("Unsupported RapidResult implementation")
        }
    }
}