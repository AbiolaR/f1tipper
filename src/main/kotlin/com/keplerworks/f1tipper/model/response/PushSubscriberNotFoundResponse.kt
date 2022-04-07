package com.keplerworks.f1tipper.model.response

import org.springframework.http.HttpStatus

data class PushSubscriberNotFoundResponse(
    val status: Int = HttpStatus.NOT_FOUND.value(),
    val message: String = "No PushSubscriber could be found using the provided name"
)
