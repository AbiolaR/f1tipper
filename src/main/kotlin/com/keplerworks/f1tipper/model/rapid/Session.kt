package com.keplerworks.f1tipper.model.rapid

import com.google.gson.annotations.SerializedName


data class Session (

  @SerializedName("id"           ) var id          : Int    = 0,
  @SerializedName("session_name" ) var sessionName : String = "",
  @SerializedName("date"         ) var date        : String = ""

)