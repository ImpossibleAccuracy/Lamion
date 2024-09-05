package com.application.lamion.exception

class BadRequest {
    val details: String

    constructor() {
        details = DETAILS
    }

    constructor(details: String) {
        this.details = details
    }

    companion object {
        const val DETAILS: String = "Bad request"
    }
}
