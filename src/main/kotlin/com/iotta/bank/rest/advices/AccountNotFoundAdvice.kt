package com.iotta.bank.rest.advices

import com.iotta.bank.rest.exceptions.ActionException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class MissingServletRequestParameterAdvice {

    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException::class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    fun missingRequestParameter(ex: MissingServletRequestParameterException): Map<String, String> {
        val name = ex.parameterName
        //logger.error("$name parameter is missing")
        return mapOf("message" to (ex.message ?: "Something bad happened :("))
    }
}

@ControllerAdvice
class AccountNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(ActionException.ActionNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFoundHandler(ex: ActionException.ActionNotFoundException): Map<String, String> =
            mapOf("message" to (ex.message ?: "Account not found"))
}

@ControllerAdvice
class AccountGeneralExceptionStateAdvice {

    @ResponseBody
    @ExceptionHandler(ActionException.ActionGeneralException::class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    fun illegalStateHandler(ex: ActionException.ActionGeneralException): Map<String, String> =
            mapOf("message" to (ex.message ?: "Something bad happened :("))
}
