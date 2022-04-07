package com.keplerworks.f1tipper.exception

import com.keplerworks.f1tipper.model.response.AccessForbiddenResponse
import com.keplerworks.f1tipper.model.response.PushSubscriberNotFoundResponse
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(AccessForbiddenException::class)
    fun handleAccessForbiddenException(exception: AccessForbiddenException) : ResponseEntity<AccessForbiddenResponse> {
        return ResponseEntity(AccessForbiddenResponse(), FORBIDDEN)
    }

    @ExceptionHandler(PushSubscriberNotFound::class)
    fun handlePushSubscriberNotFoundException(exception: PushSubscriberNotFound) : ResponseEntity<PushSubscriberNotFoundResponse> {
        return ResponseEntity(PushSubscriberNotFoundResponse(), NOT_FOUND)
    }
}