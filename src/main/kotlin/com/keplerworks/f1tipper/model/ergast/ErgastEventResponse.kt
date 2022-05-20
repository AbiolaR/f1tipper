package com.keplerworks.f1tipper.model.ergast

import com.google.gson.annotations.SerializedName


data class ErgastEventResponse (
  @SerializedName("MRData") var mRData : MRData = MRData()
) {
  data class MRData (
    @SerializedName("RaceTable") var raceTable: RaceTable = RaceTable()
  ) {
    data class RaceTable (
      @SerializedName("Races") var race: ArrayList<Race> = arrayListOf()
    ) {
      data class Race(
        @SerializedName("SprintResults") var sprintResults: ArrayList<ErgastResult> = arrayListOf(),
        @SerializedName("QualifyingResults") var qualifyingResults: ArrayList<ErgastResult> = arrayListOf(),
        @SerializedName("Results") var raceResults: ArrayList<ErgastResult> = arrayListOf()
      ) {

      }
    }
  }
}