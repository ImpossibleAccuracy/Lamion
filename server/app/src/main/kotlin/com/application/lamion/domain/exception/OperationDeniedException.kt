package com.application.lamion.domain.exception

class OperationDeniedException(message: String?) : ServiceException(message, 401)
