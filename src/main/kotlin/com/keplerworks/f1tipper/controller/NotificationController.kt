package com.keplerworks.f1tipper.controller

import com.keplerworks.f1tipper.model.PushSubscriber
import com.keplerworks.f1tipper.service.NotificationService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notification")
class NotificationController(private val service: NotificationService) {

    @GetMapping("send/{username}")
    fun sendNotificationToUser(@PathVariable username: String) {
        service.sendNotification(username)
    }

    @GetMapping("get/{username}")
    fun getPushSubscriber(@PathVariable username: String): PushSubscriber {
        return service.getPushSubscriber(username)
    }

    @PostMapping("save")
    fun savePushSubscriber(@RequestBody pushSubscriber: PushSubscriber) {
        service.savePushSubscriber(pushSubscriber)
    }
}