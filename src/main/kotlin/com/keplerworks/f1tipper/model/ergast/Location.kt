package com.keplerworks.f1tipper.model.ergast

import com.google.gson.annotations.SerializedName


data class Location (

  @SerializedName("lat"      ) var lat      : String? = null,
  @SerializedName("long"     ) var long     : String? = null,
  @SerializedName("locality" ) var locality : String? = null,
  @SerializedName("country"  ) var country  : String? = null

)