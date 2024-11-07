package com.application.lamion.server.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext


class NullOrNotBlankValidator : ConstraintValidator<NullOrNotBlank?, String?> {
    override fun initialize(parameters: NullOrNotBlank?) {}

    override fun isValid(value: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        return value == null || value.trim().isNotEmpty()
    }
}
