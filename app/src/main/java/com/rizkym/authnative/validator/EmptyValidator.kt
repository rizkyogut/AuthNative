package com.rizkym.authnative.validator

import com.rizkym.authnative.R
import com.rizkym.authnative.validator.base.BaseValidator
import com.rizkym.authnative.validator.base.ValidateResult

class EmptyValidator(private val input: String) : BaseValidator() {
    override fun validate(): ValidateResult {
        val isValid = input.isNotEmpty()
        return ValidateResult(
            isValid,
            if (isValid) R.string.text_validation_success else R.string.text_validation_error_empty_field
        )
    }
}