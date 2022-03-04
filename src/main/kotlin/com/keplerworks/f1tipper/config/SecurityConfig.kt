package com.keplerworks.f1tipper.config

import com.keplerworks.f1tipper.filter.CustomAuthenticationFilter
import com.keplerworks.f1tipper.filter.CustomAuthorizationFilter
import com.keplerworks.f1tipper.filter.FilterHelper
import com.keplerworks.f1tipper.service.FormulaUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@EnableWebSecurity
@Configuration
class SecurityConfig(@Autowired private val userService: FormulaUserService) : WebSecurityConfigurerAdapter()  {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService).passwordEncoder(BCryptPasswordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        val customAuthorizationFilter = authenticationManagerBean()?.let { CustomAuthenticationFilter(it) }
        customAuthorizationFilter?.setFilterProcessesUrl("/api/login")
        http.csrf().disable()
        http.sessionManagement().sessionCreationPolicy(STATELESS)
        http.addFilter(customAuthorizationFilter)
        http.authorizeRequests { auth ->
                auth.antMatchers("/api/login").permitAll()
                auth.antMatchers("/api/**").hasAuthority("FORMULA_USER")
            }
            .httpBasic { }

        //http.formLogin().loginPage("/login").permitAll()
        http.cors()

        http.addFilterBefore(CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter::class.java)
        //http.httpBasic().disable()

    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }

    /*@Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }*/
}