package com.keplerworks.f1tipper.type

import java.util.Optional

enum class BetType(val value: String, val repeatNumber: Int) {
    RACE("race", 20),
    QUALIFYING("qualifying", 3),
    DNF("dnf", 2);

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