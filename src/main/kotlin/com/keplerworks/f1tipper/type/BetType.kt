package com.keplerworks.f1tipper.type

import com.keplerworks.f1tipper.model.Race
import java.util.*
import kotlin.reflect.KProperty1

enum class BetType(
    val value: String,
    val dateTime: KProperty1<Race, Date>,
    val repeatNumber: Int,
    val positionPoints: Int,
    val positionGroupPoints: Int,
    val winPoints: Int
) {
    RACE("race", Race::raceStartDatetime, 20, 5, 3, 10),
    QUALIFYING("qualifying", Race::qualiStartDatetime, 3, 4, 0, 0),
    DNF("dnf", Race::raceStartDatetime, 2, 5, 0, 0);

    companion object {
        fun enumOf(value: String): BetType {
            enumValues<BetType>().forEach {
                if (value == it.value) {
                    return it
                }
            }
            throw IllegalArgumentException("No enum constant BetType.${value}")
        }
    }
}