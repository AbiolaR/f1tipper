package com.keplerworks.f1tipper.model.ergast

import com.google.gson.annotations.SerializedName


data class AverageSpeed (

  @SerializedName("units" ) var units : String? = null,
  @SerializedName("speed" ) var speed : String? = null

)