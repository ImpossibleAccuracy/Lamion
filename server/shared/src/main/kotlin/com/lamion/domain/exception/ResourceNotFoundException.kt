package com.lamion.domain.exception

class ResourceNotFoundException(message: String = "Not found") : ServiceException(message, 404)
