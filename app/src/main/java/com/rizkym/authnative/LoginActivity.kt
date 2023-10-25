package com.rizkym.authnative

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rizkym.authnative.databinding.ActivityLoginBinding
import com.rizkym.authnative.validator.EmailValidator
import com.rizkym.authnative.validator.EmptyValidator
import com.rizkym.authnative.validator.PasswordValidator
import com.rizkym.authnative.validator.base.BaseValidator
import com.rizkym.authnative.validator.base.ValidateResult

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {

            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()

            val emailValidations =
                BaseValidator.validate(EmptyValidator(email), EmailValidator(email))
            val passwordValidations =
                BaseValidator.validate(EmptyValidator(password), PasswordValidator(password))

            binding.tilEmail.error = messageValidation(emailValidations)
            binding.tilPassword.error = messageValidation(passwordValidations)
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun messageValidation(validateResult: ValidateResult) =
        if (!validateResult.isSuccess) getString(validateResult.message) else null
}