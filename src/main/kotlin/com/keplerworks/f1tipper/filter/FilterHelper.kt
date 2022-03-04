package com.keplerworks.f1tipper.filter

import com.auth0.jwt.algorithms.Algorithm

internal class FilterHelper {
    companion object {
        private const val secret = "f1secr69$"
        val algorithm: Algorithm = Algorithm.HMAC256(secret)
    }
}