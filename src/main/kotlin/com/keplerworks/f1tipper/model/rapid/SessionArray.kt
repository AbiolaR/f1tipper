package com.keplerworks.f1tipper.model.rapid

import com.google.gson.annotations.SerializedName


data class SessionArray (

  @SerializedName("id"           ) var id          : String? = null,
  @SerializedName("session_name" ) var sessionName : String? = null,
  @SerializedName("date"         ) var date        : String? = null

)