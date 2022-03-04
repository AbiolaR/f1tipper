package com.keplerworks.f1tipper.filter

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import com.keplerworks.f1tipper.filter.FilterHelper.Companion.algorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthenticationFilter @Autowired constructor(private val authManager: AuthenticationManager) : UsernamePasswordAuthenticationFilter() {
    private var expirationDuration = 1000 * 60 * 60 * 24 * 30L // 30 days

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val username: String = request?.getParameter("username") ?: ""
        val password: String = request?.getParameter("password") ?: ""
        val authenticationToken = UsernamePasswordAuthenticationToken(username, password)
        return authManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        val user: User = authResult?.principal as User
        val accessToken: String = JWT.create()
            .withSubject(user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + expirationDuration))
            .withIssuer(request?.requestURL.toString())
            .withClaim("roles", user.authorities.stream().map(GrantedAuthority::getAuthority).toList())
            .sign(algorithm)

        val refreshToken: String = JWT.create()
            .withSubject(user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + expirationDuration * 12))
            .withIssuer(request?.requestURL.toString())
            .sign(algorithm)

        val tokens: Map<String, String> = mapOf(Pair("access_token", accessToken), Pair("refresh_token", refreshToken))
        response?.contentType = APPLICATION_JSON_VALUE
        ObjectMapper().writeValue(response?.outputStream, tokens)
    }
}