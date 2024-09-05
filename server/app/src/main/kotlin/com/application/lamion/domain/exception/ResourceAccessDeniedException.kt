package com.application.lamion.domain.exception

class ResourceAccessDeniedException(message: String?) : ServiceException(message, 403)
