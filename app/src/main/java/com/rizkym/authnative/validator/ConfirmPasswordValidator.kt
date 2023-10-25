package com.rizkym.authnative.validator

import com.rizkym.authnative.R
import com.rizkym.authnative.validator.base.BaseValidator
import com.rizkym.authnative.validator.base.ValidateResult

class ConfirmPasswordValidator(private val password: String, private val confirmPassword: String) : BaseValidator() {

    override fun validate(): ValidateResult {

        //Contain Digit Number
        if (password != confirmPassword)
            return ValidateResult(false, R.string.text_validation_error_same_password)

        return ValidateResult(true, R.string.text_validation_success)
    }
}