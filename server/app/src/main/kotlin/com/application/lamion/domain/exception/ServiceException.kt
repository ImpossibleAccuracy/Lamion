package com.application.lamion.domain.exception

open class ServiceException(message: String?, val status: Int) : RuntimeException(message)
