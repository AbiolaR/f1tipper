package com.keplerworks.f1tipper.model.rapid

import com.google.gson.annotations.SerializedName

data class RapidConstructorResult (
    @SerializedName("results") var constructors : List<Constructor>
) {
    data class Constructor (
        @SerializedName("position") val position : Int = 0,
        @SerializedName("team_name") val name : String = ""
    )
}