package com.lamion.domain.exception

class OperationDeniedException(message: String?) : ServiceException(message, 403)
