package com.keplerworks.f1tipper.model.response

import org.springframework.http.HttpStatus

data class AccessForbiddenResponse(
    val status: Int = HttpStatus.FORBIDDEN.value(),
    val message: String = "Access to this object is forbidden.",
)
