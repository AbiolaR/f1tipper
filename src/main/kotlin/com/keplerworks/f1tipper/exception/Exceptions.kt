package com.keplerworks.f1tipper.exception

class BetNotFoundException(message: String) : RuntimeException(message)

class RaceNotFoundException(message: String) : RuntimeException(message)

class AccessForbiddenException(message: String = "Access to this object is forbidden.") : RuntimeException(message)

class BetItemStillOpenException(message: String = "BetItem is still open") : RuntimeException(message)

class BetSubjectNotFoundException : RuntimeException()

class RapidF1LiveSessionNotFound(message: String = "No RapidF1LiveSession could be found") : RuntimeException(message)

class PushSubscriberNotFound(message: String = "No PushSubscriber could be found using the provided name")
    : RuntimeException(message)