package com.keplerworks.f1tipper.model.rapid

import com.google.gson.annotations.SerializedName


data class RapidRaces (

  @SerializedName("meta"    ) var meta    : Meta?              = Meta(),
  @SerializedName("results" ) var results : ArrayList<RapidRaceResults> = arrayListOf()

)