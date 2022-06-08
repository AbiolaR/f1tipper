package com.keplerworks.f1tipper.model.rapid.f1live

import com.google.gson.annotations.SerializedName

data class RapidF1LiveConstructorStandingsResult (
    @SerializedName("results") var constructors : List<Constructor>
) : RapidF1LiveBetSubjectResult {
    data class Constructor (
        @SerializedName("team_name") val name : String = "",
        @SerializedName("position") val position : Int = 0
    )
}