package com.keplerworks.f1tipper.model.rapid.f1live

import com.google.gson.annotations.SerializedName

data class RapidF1LiveSessionResult (

    @SerializedName("results" ) var results : Results = Results()

) : RapidF1LiveBetSubjectResult {
    data class Results (
        @SerializedName("drivers" ) var drivers : ArrayList<Driver> = arrayListOf(),
    ) {
        data class Driver (

            @SerializedName("id"            ) var id           : Int = 0,
            @SerializedName("name"          ) var name         : String = "",
            @SerializedName("team_id"       ) var teamId       : Int = 0,
            @SerializedName("team_name"     ) var teamName     : String = "",
            @SerializedName("position"      ) var position     : Int = 0,
            @SerializedName("time"          ) var time         : String = "",
            @SerializedName("gap"           ) var gap          : String = "",
            @SerializedName("interval"      ) var interval     : String = "",
            @SerializedName("current_tyre"  ) var currentTyre  : String = "",
            @SerializedName("stops"         ) var stops        : Int = 0,
            @SerializedName("current_lap"   ) var currentLap   : Int = 0,
            @SerializedName("comments"      ) var comments     : String = "",
            @SerializedName("retired"       ) var retired      : Int = 0,
            @SerializedName("did_not_start" ) var didNotStart  : Int = 0,
            @SerializedName("disqualified"  ) var disqualified : Int = 0,
            @SerializedName("last_update"   ) var lastUpdate   : String = ""

        )      
    }

}