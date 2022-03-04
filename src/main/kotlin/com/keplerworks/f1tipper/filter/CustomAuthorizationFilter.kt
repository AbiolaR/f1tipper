package com.keplerworks.f1tipper.filter

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthorizationFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.servletPath.equals("/api/login")) {
            filterChain.doFilter(request, response)
        } else {

            val authorizationHeader: String = request.getHeader(AUTHORIZATION) ?: ""
            if (authorizationHeader.startsWith("Bearer ")) run {
                try {
                    val token = authorizationHeader.substring("Bearer ".length)
                    val verifier: JWTVerifier = JWT.require(FilterHelper.algorithm).build()
                    val decodedJWT: DecodedJWT = verifier.verify(token)
                    val username: String = decodedJWT.subject
                    val authorities: List<SimpleGrantedAuthority> = decodedJWT
                        .getClaim("roles")
                        .asArray(String::class.java)
                        .map { SimpleGrantedAuthority(it) }
                    val authenticationToken = UsernamePasswordAuthenticationToken(username, null, authorities)
                    SecurityContextHolder.getContext().authentication = authenticationToken
                } catch (exception: Exception) {
                    response.setHeader("error", exception.message)
                    response.status = FORBIDDEN.value()
                    val error: Map<String, String> = mapOf(Pair("error_message", exception.message ?: ""))
                    response.contentType = APPLICATION_JSON_VALUE
                    ObjectMapper().writeValue(response.outputStream, error)
                }
            }

            filterChain.doFilter(request, response)
        }
    }
}