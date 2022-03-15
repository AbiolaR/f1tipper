package com.keplerworks.f1tipper.type

enum class BetType(val value: String) {
    CHAMPIONSHIP("championship"),
    RACE("race");

    companion object {
        fun enumOf(value: String): BetType {
            enumValues<BetType>().forEach {
                if (value == it.value) {
                    return it
                }
            }
            throw IllegalArgumentException("No enum constant BetItemType.${value}")
        }
    }
}