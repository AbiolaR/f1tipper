package com.keplerworks.f1tipper.model.rapid

data class RapidResult(
    val betSubjects: List<BetSubject>
) : RapidBetSubjectResult {
     data class BetSubject (
        val name: String,
        val position: Int,
        val retired: Int = 0
     )
}