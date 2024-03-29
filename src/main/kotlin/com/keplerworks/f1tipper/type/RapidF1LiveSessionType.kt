package com.keplerworks.f1tipper.type

enum class RapidF1LiveSessionType(val sessionIdentifier: String, val secondarySessionIdentifier: String) {
    QUALIFYING("Qualifying 3", "Sprint Qualifying"),
    RACE("Race", "Race"),
    FASTESTLAP("FastestLap", "FastestLap"),
    NOT_NEEDED("", "");

    companion object {
        fun enumOf(identifier: String): RapidF1LiveSessionType {
            enumValues<RapidF1LiveSessionType>().forEach {
                if (identifier == it.sessionIdentifier || identifier == it.secondarySessionIdentifier) {
                    return it
                }
            }
            throw IllegalArgumentException("No enum constant RapidSessionType with identifier: $identifier")
        }
    }
}
