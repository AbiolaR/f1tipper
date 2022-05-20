package com.keplerworks.f1tipper.model.ergast

import com.google.gson.annotations.SerializedName

data class ErgastResult(
    @SerializedName("position") var position: String = "",
    @SerializedName("positionText") var positionText: String = "",
    @SerializedName("Driver") var driver: ErgastBetSubject? = null,
    @SerializedName("Constructor") var constructor: ErgastBetSubject? = null,
    @SerializedName("FastestLap") var fastestLap: FastestLap = FastestLap()
) {
    data class ErgastBetSubject (
        @SerializedName("givenName") var givenName: String = "",
        @SerializedName("familyName") var familyName: String = "",
        @SerializedName("name") var name: String? = null
    )
    data class FastestLap(
        @SerializedName("rank") var rank: String = "0"
    )
}