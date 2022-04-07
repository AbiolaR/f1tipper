package com.keplerworks.f1tipper.service

import com.google.gson.Gson
import com.keplerworks.f1tipper.exception.PushSubscriberNotFound
import com.keplerworks.f1tipper.model.PushNotification
import com.keplerworks.f1tipper.model.PushSubscriber
import com.keplerworks.f1tipper.repository.SubscriberRepository
import nl.martijndwars.webpush.Notification
import nl.martijndwars.webpush.PushService
import nl.martijndwars.webpush.Subscription
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Security


@Service
class NotificationService(
    private val subscriberRepo: SubscriberRepository
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

    fun savePushSubscriber(pushSubscriber: PushSubscriber) {
        subscriberRepo.save(pushSubscriber)
    }

    fun sendNotifications(message: String, raceId: Long? = null) {
        val pushService = PushService(publicKey, privateKey, subject)
        val pushNotification = PushNotification(message, raceId)
        val subscribers = subscriberRepo.findAll()

        subscribers.forEach { subscriber ->
            sendNotification(pushService, subscriber, pushNotification)
        }
    }

    fun sendNotification(username: String, pushNotification: PushNotification) {
        val pushService = PushService(publicKey, privateKey, subject)
        val subscriber = subscriberRepo.findByUsername(username).orElseThrow{PushSubscriberNotFound()}
        sendNotification(pushService, subscriber, pushNotification)
    }

    private fun sendNotification(pushService: PushService, subscriber: PushSubscriber, pushNotification: PushNotification) {
        val subscription = Subscription(subscriber.endpoint, Subscription.Keys(subscriber.p256dh, subscriber.auth))
        val notification = Notification(subscription, Gson().toJson(pushNotification))

        pushService.send(notification)
    }

}