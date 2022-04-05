package com.keplerworks.f1tipper.repository

import com.keplerworks.f1tipper.model.PushSubscriber
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SubscriberRepository : JpaRepository<PushSubscriber, Long> {
    fun findByUsername(username: String): Optional<PushSubscriber>
}