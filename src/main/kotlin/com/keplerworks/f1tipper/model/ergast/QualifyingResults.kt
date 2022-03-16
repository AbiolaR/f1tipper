package com.keplerworks.f1tipper.model.ergast

import com.google.gson.annotations.SerializedName

data class QualifyingResults(

    @SerializedName("number"      ) var number          : String?       = null,
    @SerializedName("position"    ) var position        : String?       = null,
    @SerializedName("Driver"      ) var ErgastDriver    : ErgastDriver? = ErgastDriver(),
    @SerializedName("Constructor" ) var Constructor     : Constructor?  = Constructor(),
    @SerializedName("Q1"          ) var Q1              : String?       = null,
    @SerializedName("Q2"          ) var Q2              : String?       = null,
    @SerializedName("Q3"          ) var Q3              : String?       = null

)
