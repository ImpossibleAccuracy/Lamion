package com.lamion.feature.projects.feature.controller.payload.request

enum class DefaultSort {
    EVENTS_COUNT,
    ERRORS_COUNT,
    FUNCTIONS_COUNT,
    DATE_CREATED;

    companion object {
        val DEFAULT = EVENTS_COUNT
    }
}
