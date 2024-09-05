package com.application.lamion.exception

class Unauthorized {
    val details: String

    constructor() {
        details = DETAILS
    }

    constructor(details: String) {
        this.details = details
    }

    companion object {
        const val DETAILS: String = "Unauthorized"
    }
}
