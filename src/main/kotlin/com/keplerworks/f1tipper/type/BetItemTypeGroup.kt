package com.keplerworks.f1tipper.type

import com.keplerworks.f1tipper.model.Race
import java.util.*
import kotlin.reflect.KProperty1

enum class BetItemTypeGroup(val value: String, val dateTime: KProperty1<Race, Date>) {
    RACE("Race", Race::raceStartDatetime),
    QUALIFYING("Qualifying", Race::qualiStartDatetime),
    CHAMPIONSHIP("Championship", Race::qualiStartDatetime)
}