package com.application.lamion.exception

class NotFound {
    val details: String

    constructor() {
        details = DETAILS
    }

    constructor(details: String) {
        this.details = details
    }

    companion object {
        const val DETAILS: String = "Not Found"
    }
}
