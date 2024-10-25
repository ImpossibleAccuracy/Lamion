package com.application.lamion.domain.exception

import org.springframework.http.HttpStatusCode
import org.springframework.web.server.ResponseStatusException

open class ServiceException(message: String?, status: Int) :
    ResponseStatusException(HttpStatusCode.valueOf(status), message)
