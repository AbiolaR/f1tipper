package com.keplerworks.f1tipper.exception

import com.keplerworks.f1tipper.model.AccessForbiddenResponse
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(AccessForbiddenException::class)
    fun handleAccessForbiddenException(exception: AccessForbiddenException) : ResponseEntity<AccessForbiddenResponse> {
        return ResponseEntity(AccessForbiddenResponse(), FORBIDDEN)
    }
}