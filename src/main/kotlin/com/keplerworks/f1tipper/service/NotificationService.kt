package com.keplerworks.f1tipper.service

import com.google.gson.Gson
import com.keplerworks.f1tipper.model.PushNotification
import com.keplerworks.f1tipper.model.PushSubscriber
import com.keplerworks.f1tipper.repository.SubscriberRepository
import com.keplerworks.f1tipper.type.BetItemTypeGroup
import nl.martijndwars.webpush.Notification
import nl.martijndwars.webpush.PushService
import nl.martijndwars.webpush.Subscription
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Security

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

    fun sendNotification(username: String) {
        val pushService = PushService(publicKey, privateKey, subject)

        val subscriber = subscriberRepo.findByUsername(username).orElseThrow()
        val subscription = Subscription(subscriber.endpoint, Subscription.Keys(subscriber.p256dh, subscriber.auth))
        val pushNotification = PushNotification("Qualifying closes in an hour.\nDon't Forget to place your bets!", 3)
        val notification = Notification(subscription, Gson().toJson(pushNotification))

        pushService.send(notification)
    }

    private fun checkImpedingBetLock() {
        val races = raceService.getAllRaces()

        races.forEach { race ->
            enumValues<BetItemTypeGroup>().forEach { type ->
                /*if (race.status()) {

                }*/
            }
        }
    }

}