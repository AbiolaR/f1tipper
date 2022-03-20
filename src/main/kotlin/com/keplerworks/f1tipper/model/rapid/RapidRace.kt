package com.keplerworks.f1tipper.model.rapid

import com.google.gson.annotations.SerializedName

data class RapidRace(
    @SerializedName("race_id" ) var raceId  : Int?    = null,
    @SerializedName("name"    ) var name    : String? = null,
    @SerializedName("country" ) var country : String? = null,
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("season"  ) var season  : String? = null,
    @SerializedName("track"   ) var track   : String? = null

)
