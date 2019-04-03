package com.jmj.mypatients.infrastructure.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.SecurityWebFilterChain


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfiguration {

    @Bean
    fun securitygWebFilterChain(
            http: ServerHttpSecurity): SecurityWebFilterChain {
        http.csrf().disable()
        return http.authorizeExchange()
                .anyExchange().authenticated()
                .and().httpBasic()
                .and().build()
    }

    @Bean
    fun userDetailsService(): MapReactiveUserDetailsService {
        val user = User
                .withUsername("user")
                .password("{noop}password")
                .roles("USER")
                .build()
        return MapReactiveUserDetailsService(user)
    }
}