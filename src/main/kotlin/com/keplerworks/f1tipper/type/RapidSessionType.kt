package com.keplerworks.f1tipper.type

enum class RapidSessionType(val sessionIdentifier: String) {
    QUALIFYING("Qualifying 3"),
    RACE("Race"),
    FASTESTLAP("FastestLap"),
    NOTNEEDED("");

    companion object {
        fun enumOf(identifier: String): RapidSessionType {
            enumValues<RapidSessionType>().forEach {
                if (identifier == it.sessionIdentifier) {
                    return it
                }
            }
            throw IllegalArgumentException("No enum constant RapidSessionType with identifier: $identifier")
        }
    }
}
