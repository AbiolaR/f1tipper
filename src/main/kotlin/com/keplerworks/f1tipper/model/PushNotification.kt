package com.keplerworks.f1tipper.model

import com.google.gson.annotations.SerializedName

data class PushNotification(
    val message: String,
    val raceId: Long? = null,
    val raceAddition: String = if (raceId != null) "race/$raceId" else "",
    @SerializedName("notification") val notification: Notification = Notification(body = message,
                                        data = Notification.Data(url = "https://media-server.casacam.net/$raceAddition"))
) {
    data class Notification (
        @SerializedName("body") var body: String,
        @SerializedName("title") val title: String = "F1 Tipper",
        @SerializedName("icon") val icon: String = "https://media-server.casacam.net/assets/icons/apple-touch-icon.png",
        @SerializedName("badge") val badge: String = "https://media-server.casacam.net/assets/icons/badge.png",
        @SerializedName("vibrate") val vibrate: List<Int> = listOf(100, 50, 100),
        @SerializedName("data") val data: Data = Data()
    ) {
        data class Data (
            @SerializedName("url") val url: String = "https://media-server.casacam.net/"
        )
    }
}
