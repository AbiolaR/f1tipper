package com.keplerworks.f1tipper.model.ergast

import com.google.gson.annotations.SerializedName


data class Results (

    @SerializedName("number"       ) var number       : String?         = null,
    @SerializedName("position"     ) var position     : String?         = null,
    @SerializedName("positionText" ) var positionText : String?         = null,
    @SerializedName("points"       ) var points       : String?         = null,
    @SerializedName("Driver"       ) var ErgastDriver : ErgastDriver?   = ErgastDriver(),
    @SerializedName("Constructor"  ) var Constructor  : Constructor?    = Constructor(),
    @SerializedName("grid"         ) var grid         : String?         = null,
    @SerializedName("laps"         ) var laps         : String?         = null,
    @SerializedName("status"       ) var status       : String?         = null,
    @SerializedName("Time"         ) var Time         : Time?           = Time(),
    @SerializedName("FastestLap"   ) var FastestLap   : FastestLap?     = FastestLap()

)