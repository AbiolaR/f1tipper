package com.keplerworks.f1tipper.model.ergast

import com.google.gson.annotations.SerializedName


data class FastestLap (

  @SerializedName("rank"         ) var rank         : String?       = null,
  @SerializedName("lap"          ) var lap          : String?       = null,
  @SerializedName("Time"         ) var Time         : Time?         = Time(),
  @SerializedName("AverageSpeed" ) var AverageSpeed : AverageSpeed? = AverageSpeed()

)