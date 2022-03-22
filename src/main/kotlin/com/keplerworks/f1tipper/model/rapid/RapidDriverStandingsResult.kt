package com.keplerworks.f1tipper.model.rapid

import com.google.gson.annotations.SerializedName

data class RapidDriverStandingsResult(
    @SerializedName("results") var drivers : List<Driver>
) : RapidBetSubjectResult {
    data class Driver(
        @SerializedName("driver_name") val name: String = "",
        @SerializedName("position") val position: Int = 0
    )
}
