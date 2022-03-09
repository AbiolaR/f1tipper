package com.keplerworks.f1tipper.model.ergast

import com.google.gson.annotations.SerializedName


data class RaceTable (

  @SerializedName("season" ) var season : String?          = null,
  @SerializedName("round"  ) var round  : String?          = null,
  @SerializedName("Races"  ) var Races  : ArrayList<Races> = arrayListOf()

)