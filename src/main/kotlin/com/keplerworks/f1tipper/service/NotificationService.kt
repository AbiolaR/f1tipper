package com.keplerworks.f1tipper.service

import com.google.gson.Gson
import com.keplerworks.f1tipper.model.PushNotification
import com.keplerworks.f1tipper.model.PushSubscriber
import com.keplerworks.f1tipper.repository.SubscriberRepository
import com.keplerworks.f1tipper.type.BetItemStatus
import com.keplerworks.f1tipper.type.BetItemTypeGroup
import nl.martijndwars.webpush.Notification
import nl.martijndwars.webpush.PushService
import nl.martijndwars.webpush.Subscription
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Security
import java.util.*


@Service
class NotificationService(
    private val subscriberRepo: SubscriberRepository,
    private val raceService: RaceService
) {
    init {
        Security.addProvider(BouncyCastleProvider())
    }

    @Value("\${vapid.key.public}")
    private val publicKey: String = ""

    @Value("\${vapid.key.private}")
    private val privateKey: String = ""

    @Value("\${vapid.subject}")
    private val subject: String = ""


    fun getPushSubscriber(username: String): PushSubscriber {
        return subscriberRepo.findByUsername(username).orElseThrow()
    }

    fun savePushSubscriber(pushSubscriber: PushSubscriber) {
        subscriberRepo.save(pushSubscriber)
    }

    fun sendNotifications(message: String, raceId: Long? = null) {
        val pushService = PushService(publicKey, privateKey, subject)
        val pushNotification = PushNotification(message, raceId)

        val subscribers = subscriberRepo.findAll()

        subscribers.forEach { subscriber ->
            val subscription = Subscription(subscriber.endpoint, Subscription.Keys(subscriber.p256dh, subscriber.auth))
            val notification = Notification(subscription, Gson().toJson(pushNotification))

            pushService.send(notification)
        }

    }

    fun sendNotification(username: String, pushNotification: PushNotification) {
        val pushService = PushService(publicKey, privateKey, subject)

        val subscriber = subscriberRepo.findByUsername(username).orElseThrow()
        val subscription = Subscription(subscriber.endpoint, Subscription.Keys(subscriber.p256dh, subscriber.auth))
        val notification = Notification(subscription, Gson().toJson(pushNotification))

        pushService.send(notification)
    }

    fun checkImpedingBetLock() {
        val races = raceService.getAllRaces()
        var raceId: Long? = null
        var typeGroup: BetItemTypeGroup? = null

        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.HOUR_OF_DAY, 1)
        cal.add(Calendar.SECOND, 5)
        val oneHourFromNow = cal.time

        races.forEach raceLoop@ { race ->
            enumValues<BetItemTypeGroup>().forEach { type ->
                if (race.status(type) == BetItemStatus.OPEN
                    && race.status(type, oneHourFromNow) == BetItemStatus.LOCKED) {
                    typeGroup = type
                    raceId = race.id
                    return@raceLoop
                }
            }
        }

        if (raceId != null && typeGroup != null) {
            sendNotifications(typeGroup!!.value, raceId!!)
        }

    }

}