package com.keplerworks.f1tipper.model

import com.keplerworks.f1tipper.type.BetItemStatus
import com.keplerworks.f1tipper.type.BetItemType
import com.keplerworks.f1tipper.type.BetItemTypeGroup
import com.keplerworks.f1tipper.type.BetType
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Race(
    @Id
    val id: Long,
    val round: Int,
    val name: String,
    val title: String,
    val flagImgUrl: String,
    val trackSvg: String,
    val raceStartDatetime: Date,
    val qualiStartDatetime: Date,
    val country: String,

) {
    val dateRange: String get() {
        if (round == 0) {
            return "2022"
        }
        val dateRange = StringBuilder()
        val calendar = Calendar.getInstance()
        calendar.time = this.qualiStartDatetime
        dateRange.append(calendar.get(Calendar.DAY_OF_MONTH)).append(" - ")
        calendar.time = this.raceStartDatetime
        dateRange.append(calendar.get(Calendar.DAY_OF_MONTH)).append(" ")
            .append(SimpleDateFormat("MMM", Locale.ENGLISH).format(calendar.time))
        return dateRange.toString()
    }

    fun status(betType: BetType): BetItemStatus {
        return when (betType) {
            BetType.RACE -> status(BetItemTypeGroup.RACE)
            BetType.CHAMPIONSHIP -> status(BetItemTypeGroup.CHAMPIONSHIP)
        }
    }

    fun status(betItemType: BetItemType): BetItemStatus {
        return status(betItemType.toBetItemTypeGroup())
    }

    fun status(betItemTypeGroup: BetItemTypeGroup, compareDate: Date = Date()): BetItemStatus {
        val date = betItemTypeGroup.dateTime.get(this)

        if (date.after(compareDate)) {
            return BetItemStatus.OPEN
        }
        return BetItemStatus.LOCKED
    }
}
