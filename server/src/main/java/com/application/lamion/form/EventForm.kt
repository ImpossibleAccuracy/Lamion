package com.application.lamion.form

class EventForm {
    var title: String? = null

    val isValid: Boolean
        get() = ((title != null && !title!!.isBlank()))
}
