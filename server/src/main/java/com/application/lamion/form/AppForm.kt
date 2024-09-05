package com.application.lamion.form

data class AppForm(
    val title: String?,
    val description: String?,
) {
    val isValid: Boolean
        get() = !title.isNullOrBlank()
}
