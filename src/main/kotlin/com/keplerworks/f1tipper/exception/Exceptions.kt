package com.keplerworks.f1tipper.exception

class DriverNotFoundException(message: String) : RuntimeException(message)

class BetNotFoundException(message: String) : RuntimeException(message)

class RaceNotFoundException(message: String) : RuntimeException(message)

class BetItemNotFoundException(message: String) : RuntimeException(message)

class AccessForbiddenException(message: String = "Access to this object is forbidden.") : RuntimeException(message)

class BetItemStillOpenException(message: String = "BetItem is still open") : RuntimeException(message)

class ResultNotFoundException(message: String = "No Result matching the BetItem could be found") : RuntimeException(message)