package com.rizkym.authnative

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rizkym.authnative.databinding.ActivityForgetPasswordBinding
import com.rizkym.authnative.databinding.ActivityLoginBinding
import com.rizkym.authnative.validator.EmailValidator
import com.rizkym.authnative.validator.EmptyValidator
import com.rizkym.authnative.validator.base.BaseValidator
import com.rizkym.authnative.validator.base.ValidateResult

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.edEmail.text.toString()
        val emailValidations = BaseValidator.validate(EmptyValidator(email), EmailValidator(email))
        binding.tilEmail.error = messageValidation(emailValidations)
    }

    private fun messageValidation(validateResult: ValidateResult) =
        if (!validateResult.isSuccess) getString(validateResult.message) else null
}