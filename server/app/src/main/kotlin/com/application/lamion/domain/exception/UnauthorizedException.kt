package com.application.lamion.domain.exception

class UnauthorizedException(message: String?) :
    ServiceException(message, 401)
