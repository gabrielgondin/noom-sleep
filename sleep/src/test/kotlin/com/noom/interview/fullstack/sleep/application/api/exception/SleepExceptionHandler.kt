package com.noom.interview.fullstack.sleep.application.api.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpClientErrorException


@RestControllerAdvice
class SleepExceptionHandler {
    @ExceptionHandler(HttpClientErrorException.NotFound::class)
    fun handleNotFoundException(ex: HttpClientErrorException.NotFound): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(
                code = HttpStatus.NOT_FOUND.toString(),
                message = "Resource not found"
            ))
    }
}


data class ErrorResponse(val code: String, val message: String?)