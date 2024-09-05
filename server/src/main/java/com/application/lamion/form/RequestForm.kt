package com.application.lamion.form

class RequestForm {
    var device: String? = null

    val isValid: Boolean
        get() {
            if (device == null) return true

            val d = device!!.trim()
            return (d.length > 0 && d.length <= 50)
        }
}
