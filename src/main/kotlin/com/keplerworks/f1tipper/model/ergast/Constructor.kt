package com.keplerworks.f1tipper.model.ergast

import com.google.gson.annotations.SerializedName


data class Constructor (

  @SerializedName("constructorId" ) var constructorId : String? = null,
  @SerializedName("url"           ) var url           : String? = null,
  @SerializedName("name"          ) var name          : String? = null,
  @SerializedName("nationality"   ) var nationality   : String? = null

)