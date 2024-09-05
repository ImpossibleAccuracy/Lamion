package com.application.lamion.domain.exception

class ResourceNotFoundException(message: String = "Not found") : ServiceException(message, 404)
