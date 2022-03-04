package com.keplerworks.f1tipper.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

class DriverNotFoundException(message: String) : RuntimeException(message)

class RaceBetItemNotFoundException(message: String) : RuntimeException(message)

class RaceNotFoundException(message: String) : RuntimeException(message)

class BetNotFoundException(message: String) : RuntimeException(message)

class AccessForbiddenException(message: String = "Access to this object is forbidden.") : RuntimeException(message)