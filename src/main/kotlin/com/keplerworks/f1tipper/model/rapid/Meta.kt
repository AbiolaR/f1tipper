package com.keplerworks.f1tipper.model.rapid

import com.google.gson.annotations.SerializedName


data class Meta (

  @SerializedName("title"       ) var title       : String? = null,
  @SerializedName("description" ) var description : String? = null,
  @SerializedName("fields"      ) var fields      : Fields? = Fields()



) {
  data class Fields (

    @SerializedName("race_id"       ) var raceId       : String?       = null,
    @SerializedName("name"          ) var name         : String?       = null,
    @SerializedName("country"       ) var country      : String?       = null,
    @SerializedName("status"        ) var status       : String?       = null,
    @SerializedName("season"        ) var season       : String?       = null,
    @SerializedName("start_date"    ) var startDate    : String?       = null,
    @SerializedName("end_date"      ) var endDate      : String?       = null,
    @SerializedName("track"         ) var track        : String?       = null,
    @SerializedName("session_array" ) var sessionArray : SessionArray? = SessionArray()

  )
}