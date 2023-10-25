package com.rizkym.authnative.validator

import com.rizkym.authnative.R
import com.rizkym.authnative.validator.base.BaseValidator
import com.rizkym.authnative.validator.base.ValidateResult

class PasswordValidator(private val password: String) : BaseValidator() {
    private val minPasswordLength = 8

    private fun containsDigit(input: String): Boolean {
        val regex = Regex(".*\\d.*")
        return regex.matches(input)
    }

    override fun validate(): ValidateResult {
        //Minimum Length
        if (password.length < minPasswordLength)
            return ValidateResult(false, R.string.text_validation_error_min_pass_length)

        //Uppercase Validation
        if (password.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } == null)
            return ValidateResult(false, R.string.text_validation_error_uppercase)

        //Lowercase Validation
        if (password.filter { it.isLetter() }.firstOrNull { it.isLowerCase() } == null)
            return ValidateResult(false, R.string.text_validation_error_lowercase)

        //Contain Digit Number
        if (!containsDigit(password))
            return ValidateResult(false, R.string.text_validation_error_contain_number)

        return ValidateResult(true, R.string.text_validation_success)
    }
}