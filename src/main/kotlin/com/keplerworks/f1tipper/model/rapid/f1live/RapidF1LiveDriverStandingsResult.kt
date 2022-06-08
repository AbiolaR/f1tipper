package com.keplerworks.f1tipper.model.rapid.f1live

import com.google.gson.annotations.SerializedName

data class RapidF1LiveDriverStandingsResult(
    @SerializedName("results") var drivers : List<Driver>
) : RapidF1LiveBetSubjectResult {
    data class Driver(
        @SerializedName("driver_name") val name: String = "",
        @SerializedName("position") val position: Int = 0
    )
}
