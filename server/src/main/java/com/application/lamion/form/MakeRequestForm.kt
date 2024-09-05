package com.application.lamion.form

class MakeRequestForm {
    var appId: Int = 0
    var event: String? = null
    var device: String? = null

    val isValid: Boolean
        get() = (appId >= 0 && (event != null && !event!!.isBlank()))
}
