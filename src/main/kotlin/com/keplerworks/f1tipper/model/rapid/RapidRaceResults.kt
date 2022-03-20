package com.keplerworks.f1tipper.model.rapid

import com.google.gson.annotations.SerializedName


data class RapidRaceResults (

  @SerializedName("race_id"    ) var raceId    : Int = 0,
  @SerializedName("name"       ) var name      : String = "",
  @SerializedName("country"    ) var country   : String = "",
  @SerializedName("status"     ) var status    : String = "",
  @SerializedName("season"     ) var season    : String = "",
  @SerializedName("track"      ) var track     : String = "",
  @SerializedName("latitude"   ) var latitude  : String = "",
  @SerializedName("longitude"  ) var longitude : String = "",
  @SerializedName("start_date" ) var startDate : String = "",
  @SerializedName("end_date"   ) var endDate   : String = "",
  @SerializedName("sessions"   ) var sessions  : ArrayList<Session> = arrayListOf()

)