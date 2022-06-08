package com.keplerworks.f1tipper.model.rapid.f1live

data class RapidF1LiveResult(
    val betSubjects: List<BetSubject>
) : RapidF1LiveBetSubjectResult {
     data class BetSubject (
        val name: String,
        val position: Int,
        val retired: Int = 0,
        val gap: String = ""
     )
}