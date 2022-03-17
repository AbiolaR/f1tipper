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
    RACE("race", Race::raceStartDatetime, 20, 15, 5, 20),
    QUALIFYING("qualifying", Race::qualiStartDatetime, 5, 5, 0, 5),
    DNF("dnf", Race::raceStartDatetime, 2, 5, 0, 5),
    DRIVER("driver", Race::qualiStartDatetime, 20,400, 200,500),
    CONSTRUCTOR("constructor", Race::qualiStartDatetime, 10, 600, 200, 800);

    val resultAmount: Int get() = when (this) {
        DNF -> 20
        else -> repeatNumber
    }

    fun isChampionshipType(): Boolean {
        return when(this) {
            DRIVER -> true
            CONSTRUCTOR -> true
            else -> false
        }
    }

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