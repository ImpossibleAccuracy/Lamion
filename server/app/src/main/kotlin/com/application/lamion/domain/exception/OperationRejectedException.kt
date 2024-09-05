package com.application.lamion.domain.exception

class OperationRejectedException(message: String = "No such permissions") : ServiceException(message, 401)
