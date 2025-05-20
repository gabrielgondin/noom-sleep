package com.noom.interview.fullstack.sleep.application.api.exception

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpClientErrorException
import org.slf4j.LoggerFactory


@RestControllerAdvice
class SleepExceptionHandler {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(HttpClientErrorException.NotFound::class)
    fun handleNotFoundException(ex: HttpClientErrorException.NotFound): ResponseEntity<ErrorResponse> {
        logger.error(ex.message)
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(
                code = HttpStatus.NOT_FOUND.toString(),
                message = "Resource not found"
            ))
    }

    @ExceptionHandler(OverlapSleepTimeException::class)
    fun handleOverlapSleepTimeException(ex: OverlapSleepTimeException): ResponseEntity<ErrorResponse> {
        logger.error(ex.message, ex)
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse(
                code = HttpStatus.CONFLICT.toString(),
                message = "Sleep data has already been recorded for this period"
            ))
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<ErrorResponse> {
        logger.error(ex.message, ex)
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse(
                code = HttpStatus.CONFLICT.toString(),
                message = "Conflict to save data"
            ))
    }
}


data class ErrorResponse(val code: String, val message: String?)