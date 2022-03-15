package com.keplerworks.f1tipper.type

import com.keplerworks.f1tipper.model.Race
import java.util.*
import kotlin.reflect.KProperty1

enum class BetItemType(
    val value: String,
    val dateTime: KProperty1<Race, Date>,
    val repeatNumber: Int,
    val positionPoints: Int,
    val positionGroupPoints: Int,
    val winPoints: Int
) {
    RACE("race", Race::raceStartDatetime, 20, 9, 3, 12),
    QUALIFYING("qualifying", Race::qualiStartDatetime, 5, 4, 0, 0),
    DNF("dnf", Race::raceStartDatetime, 2, 5, 0, 0),
    DRIVER("driver", Race::qualiStartDatetime, 20, 15, 0, 20),
    CONSTRUCTOR("constructor", Race::qualiStartDatetime, 10, 15, 0, 20);

    companion object {
        fun enumOf(value: String): BetItemType {
            enumValues<BetItemType>().forEach {
                if (value == it.value) {
                    return it
                }
            }
            throw IllegalArgumentException("No enum constant BetItemType.${value}")
        }
    }
}