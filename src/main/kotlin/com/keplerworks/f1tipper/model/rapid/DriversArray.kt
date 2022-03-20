package com.keplerworks.f1tipper.model.rapid

import com.google.gson.annotations.SerializedName


data class DriversArray (

    @SerializedName("driver"        ) var driver        : String = "",
    @SerializedName("position"      ) var position      : String = "",
    @SerializedName("speed"         ) var speed         : String = "",
    @SerializedName("driver_id"     ) var driverId      : String = "",
    @SerializedName("team_name"     ) var teamName      : String = "",
    @SerializedName("team_id"       ) var teamId        : String = "",
    @SerializedName("id"            ) var id            : String = "",
    @SerializedName("name"          ) var name          : String = "",
    @SerializedName("time"          ) var time          : String = "",
    @SerializedName("gap"           ) var gap           : String = "",
    @SerializedName("interval"      ) var interval      : String = "",
    @SerializedName("current_tyre"  ) var currentTyre   : String = "",
    @SerializedName("stops"         ) var stops         : String = "",
    @SerializedName("current_lap"   ) var currentLap    : String = "",
    @SerializedName("comments"      ) var comments      : String = "",
    @SerializedName("retired"       ) var retired       : String = "",
    @SerializedName("did_not_start" ) var didNotStart   : String = "",
    @SerializedName("disqualified"  ) var disqualified  : String = "",
    @SerializedName("last_update"   ) var lastUpdate    : String = ""

)