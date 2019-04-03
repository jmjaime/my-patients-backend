package com.jmj.mypatients.infrastructure.delivery

import com.jmj.mypatients.model.errors.ObjectAlreadyExistsException
import com.jmj.mypatients.model.errors.ObjectNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.handler.WebFluxResponseStatusExceptionHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class ExceptionHandler : WebFluxResponseStatusExceptionHandler() {

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        when (ex) {
            is ObjectNotFoundException -> mapToCode(exchange, HttpStatus.NOT_FOUND)
            is ObjectAlreadyExistsException -> mapToCode(exchange, HttpStatus.CONFLICT)
        }
        return Mono.error(ex)
    }

    private fun mapToCode(exchange: ServerWebExchange, httpStatus: HttpStatus) {
        exchange.response.statusCode = httpStatus
    }

}