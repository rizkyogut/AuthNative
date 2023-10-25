package com.rizkym.authnative.validator

import android.text.TextUtils
import com.rizkym.authnative.R
import com.rizkym.authnative.validator.base.BaseValidator
import com.rizkym.authnative.validator.base.ValidateResult

class EmailValidator(private val email: String) : BaseValidator() {
    override fun validate(): ValidateResult {
        val isValid =
            !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        return ValidateResult(
            isValid,
            if (isValid) R.string.text_validation_success else R.string.text_validation_error_email
        )
    }
}