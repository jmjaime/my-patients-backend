package com.jmj.mypatients.infrastructure.delivery

import com.jmj.mypatients.domain.errors.ObjectAlreadyExistsException
import com.jmj.mypatients.domain.errors.ObjectNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus


@ControllerAdvice
class ControllerErrorHandler {

    @ResponseBody
    @ExceptionHandler(ObjectNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun objectNotFoundException(ex: ObjectNotFoundException) = ex.message

    @ResponseBody
    @ExceptionHandler(ObjectAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun objectAlreadyExistsException(ex: ObjectAlreadyExistsException) = ex.message

}