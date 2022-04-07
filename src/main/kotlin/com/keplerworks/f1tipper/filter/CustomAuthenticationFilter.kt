package com.keplerworks.f1tipper.filter

import com.auth0.jwt.JWT
import com.fasterxml.jackson.databind.ObjectMapper
import com.keplerworks.f1tipper.dto.LoginDataDTO
import com.keplerworks.f1tipper.dto.UserDTO
import com.keplerworks.f1tipper.filter.FilterHelper.Companion.algorithm
import com.keplerworks.f1tipper.service.FormulaUserService
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

class CustomAuthenticationFilter (
    private val authManager: AuthenticationManager,
    private val userService: FormulaUserService) : UsernamePasswordAuthenticationFilter() {
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
        val roles = user.authorities.stream().map(GrantedAuthority::getAuthority).toList()
        val accessToken: String = JWT.create()
            .withSubject(user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + expirationDuration))
            .withIssuer(request?.requestURL.toString())
            .withClaim("roles", roles)
            .sign(algorithm)

        val refreshToken: String = JWT.create()
            .withSubject(user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + expirationDuration * 12))
            .withIssuer(request?.requestURL.toString())
            .sign(algorithm)

        //val data: Map<String, Any> = mapOf(Pair("access_token", accessToken), Pair("refresh_token", refreshToken), Pair("roles", roles))

        val formulaUser = userService.getUser(user.username)

        val data = UserDTO(
            formulaUser.leagues.first(),
            formulaUser.leagues,
            LoginDataDTO(formulaUser.username, accessToken, refreshToken, roles)
        )

        response?.contentType = APPLICATION_JSON_VALUE
        ObjectMapper().writeValue(response?.outputStream, data)
    }
}