package com.rizkym.authnative.validator

import com.rizkym.authnative.R
import com.rizkym.authnative.validator.base.BaseValidator
import com.rizkym.authnative.validator.base.ValidateResult

class NameValidator(private val name: String) : BaseValidator() {
    private val minNameLength = 3
    private val maxNameLength = 12

    override fun validate(): ValidateResult {
        if (name.length < minNameLength)
            return ValidateResult(false, R.string.text_validation_error_min_name_length)

        if (name.length > maxNameLength)
            return ValidateResult(false, R.string.text_validation_error_max_name_length)
        return ValidateResult(true, R.string.text_validation_success)
    }
}