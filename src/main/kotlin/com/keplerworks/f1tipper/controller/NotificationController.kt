package com.keplerworks.f1tipper.controller

import com.keplerworks.f1tipper.model.PushNotification
import com.keplerworks.f1tipper.model.PushSubscriber
import com.keplerworks.f1tipper.service.NotificationService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notification")
class NotificationController(private val service: NotificationService) {

    @PostMapping("send/{username}")
    fun sendNotificationToUser(@PathVariable username: String, @RequestBody pushNotification: PushNotification) {
        service.sendNotification(username, pushNotification)
    }

    @PostMapping("save")
    fun savePushSubscriber(@RequestBody pushSubscriber: PushSubscriber) {
        service.savePushSubscriber(pushSubscriber)
    }
}