package com.keplerworks.f1tipper.type

enum class BetItemType(
    val value: String,
    val repeatNumber: Int,
    val positionPoints: Int,
    val positionGroupPoints: Int,
    val winPoints: Int
) {
    RACE("race", 20, 15, 5, 20),
    QUALIFYING("qualifying", 5, 5, 0, 5),
    DNF("dnf", 2, 5, 0, 5),
    DRIVER("driver", 20,400, 200,500),
    CONSTRUCTOR("constructor", 10, 600, 200, 800);

    fun isChampionshipType(): Boolean {
        return when(this) {
            DRIVER -> true
            CONSTRUCTOR -> true
            else -> false
        }
    }

    fun toBetItemTypeGroup(): BetItemTypeGroup {
        return when (this) {
            QUALIFYING -> BetItemTypeGroup.QUALIFYING
            CONSTRUCTOR -> BetItemTypeGroup.CHAMPIONSHIP
            DRIVER -> BetItemTypeGroup.CHAMPIONSHIP
            RACE -> BetItemTypeGroup.RACE
            DNF -> BetItemTypeGroup.RACE
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