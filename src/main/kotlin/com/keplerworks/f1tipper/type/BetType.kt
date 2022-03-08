package com.keplerworks.f1tipper.type

import java.util.Optional

enum class BetType(
    val value: String,
    val repeatNumber: Int,
    val positionPoints: Int,
    val positionGroupPoints: Int,
    val winPoints: Int
) {
    RACE("race", 20, 5, 3, 10),
    QUALIFYING("qualifying", 3, 4, 0, 0),
    DNF("dnf", 2, 5, 0, 0);

    companion object {
        fun enumOf(value: String): BetType {
            enumValues<BetType>().forEach {
                if (value == it.value) {
                    return it
                }
            }
            throw IllegalArgumentException("No enum contant BetType.${value}")
        }
    }
}