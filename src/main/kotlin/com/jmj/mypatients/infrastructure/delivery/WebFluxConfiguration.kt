package com.jmj.mypatients.infrastructure.delivery

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.handler.WebFluxResponseStatusExceptionHandler
import org.springframework.web.server.WebExceptionHandler

@EnableWebFlux
@Configuration
class WebFluxConfiguration : WebFluxConfigurer {

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.defaultCodecs().enableLoggingRequestDetails(true)
    }

    @Bean
    fun exceptionHandler() : WebFluxResponseStatusExceptionHandler = ExceptionHandler()
}