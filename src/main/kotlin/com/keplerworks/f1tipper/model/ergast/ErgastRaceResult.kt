package com.keplerworks.f1tipper.model.ergast

import com.google.gson.annotations.SerializedName


data class ErgastRaceResult (

  @SerializedName("MRData" ) var MRData : MRData? = MRData()

)