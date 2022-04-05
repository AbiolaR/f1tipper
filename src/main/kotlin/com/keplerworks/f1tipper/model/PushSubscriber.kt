package com.keplerworks.f1tipper.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class PushSubscriber(
    @Id
    val username: String,
    val endpoint: String,
    val auth: String,
    val p256dh: String
)
