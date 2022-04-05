package com.keplerworks.f1tipper.config

import com.github.shyiko.skedule.Schedule
import com.keplerworks.f1tipper.service.NotificationService
import com.keplerworks.f1tipper.service.RaceService
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalTime
import java.time.Month
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit


@Configuration
class ScheduledJobConfig(private val notificationService: NotificationService, private val raceService: RaceService) {
    val logger = KotlinLogging.logger {}

    @Bean
    fun createSchedules() {
        val races = raceService.getAllRaces()
        val executor = ScheduledThreadPoolExecutor(1)
        executor.removeOnCancelPolicy = true
        val now = ZonedDateTime.now()

        races.forEach { race ->
            val dates = mutableMapOf(Pair("Qualifying", race.qualiStartDatetime), Pair("Race", race.raceStartDatetime))
            if (race.round == 0) {
                dates["Championship"] = dates["Qualifying"]!!
                dates.remove("Race")
                dates.remove("Qualifying")
            }

            dates.forEach { dateTime ->
                val date = Calendar.getInstance()
                date.time = dateTime.value
                val closeTime = "%02d:%02d".format(date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE))
                date.add(Calendar.HOUR_OF_DAY, -1)

                executor.schedule(
                    {
                        val message = "${dateTime.key} closes at $closeTime" +
                                "\nDon't forget to place your bets!"
                        notificationService.sendNotifications(message, race.id)
                        logger.info { "${dateTime.key} notification was sent for race id ${race.id}" }
                    },
                    Schedule.at(LocalTime.of(date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE)))
                        .nth(date.get(Calendar.DAY_OF_MONTH)).of(Month.of(date.get(Calendar.MONTH) + 1))
                        .next(now).toEpochSecond() - now.toEpochSecond(),
                    TimeUnit.SECONDS
                )
            }

        }









    }
}