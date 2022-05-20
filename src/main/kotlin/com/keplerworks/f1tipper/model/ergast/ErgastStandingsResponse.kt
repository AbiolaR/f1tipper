package com.keplerworks.f1tipper.model.ergast

import com.google.gson.annotations.SerializedName

data class ErgastStandingsResponse (
    @SerializedName("MRData") var mRData : MRData = MRData()
) {
    data class MRData (
        @SerializedName("StandingsTable") var standingsTable: StandingsTable = StandingsTable()
    ) {
        data class StandingsTable (
            @SerializedName("StandingsLists") var standingsLists: ArrayList<StandingsList> = arrayListOf()
        ) {
            data class StandingsList (
                @SerializedName("DriverStandings") var driverStandings: ArrayList<ErgastResult> = arrayListOf(),
                @SerializedName("ConstructorStandings") var constructorStandings: ArrayList<ErgastResult> = arrayListOf()
            )

            /*data class Standing (
                @SerializedName("position") var position: String = "",
                @SerializedName("Driver") var driver: Driver = Driver()
            ) {
                data class Driver (
                    @SerializedName("givenName") var givenName: String = "",
                    @SerializedName("familyName") var familyName: String = ""
                )
            }*/
        }
    }
}
