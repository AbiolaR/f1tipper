package com.keplerworks.f1tipper.model.rapid

import com.google.gson.annotations.SerializedName

data class RapidConstructorStandingsResult (
    @SerializedName("results") var constructors : List<Constructor>
) : RapidBetSubjectResult {
    data class Constructor (
        @SerializedName("team_name") val name : String = "",
        @SerializedName("position") val position : Int = 0
    )
}