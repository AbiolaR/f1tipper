package com.keplerworks.f1tipper.model.ergast

import com.google.gson.annotations.SerializedName


data class Races (

    @SerializedName("season"              ) var season            : String?            = null,
    @SerializedName("round"               ) var round             : String?            = null,
    @SerializedName("url"                 ) var url               : String?            = null,
    @SerializedName("raceName"            ) var raceName          : String?            = null,
    @SerializedName("Circuit"             ) var Circuit           : Circuit?           = Circuit(),
    @SerializedName("date"                ) var date              : String?            = null,
    @SerializedName("time"                ) var time              : String?            = null,
    @SerializedName("Results"             ) var Results           : ArrayList<ErgastResults> = arrayListOf(),
    @SerializedName("QualifyingResults"   ) var QualifyingResults : ArrayList<QualifyingResults> = arrayListOf()
)