package com.application.lamion.server.exception

import com.application.lamion.domain.exception.ServiceException
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException::class)
    fun handleResourceResourceNotFound(
        e: ServiceException,
        response: HttpServletResponse
    ) {
        response.sendError(e.status, e.message)
    }
}