package com.rizkym.authnative

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rizkym.authnative.databinding.ActivityRegisterBinding
import com.rizkym.authnative.validator.ConfirmPasswordValidator
import com.rizkym.authnative.validator.EmailValidator
import com.rizkym.authnative.validator.EmptyValidator
import com.rizkym.authnative.validator.NameValidator
import com.rizkym.authnative.validator.PasswordValidator
import com.rizkym.authnative.validator.base.BaseValidator
import com.rizkym.authnative.validator.base.ValidateResult

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {

            val name = binding.edName.text.toString()
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()
            val confirmPassword = binding.edConfirmPassword.text.toString()

            val userValidation =
                BaseValidator.validate(EmptyValidator(name), NameValidator(name))
            val emailValidations =
                BaseValidator.validate(EmptyValidator(email), EmailValidator(email))
            val passwordValidations =
                BaseValidator.validate(EmptyValidator(password), PasswordValidator(password))
            val passwordConfirmValidations =
                BaseValidator.validate(EmptyValidator(confirmPassword), ConfirmPasswordValidator(password, confirmPassword))

            binding.tilName.error = messageValidation(userValidation)
            binding.tilEmail.error = messageValidation(emailValidations)
            binding.tilPassword.error = messageValidation(passwordValidations)
            binding.tilConfirmPassword.error = messageValidation(passwordConfirmValidations)


        }
    }


    private fun messageValidation(validateResult: ValidateResult) =
        if (!validateResult.isSuccess) getString(validateResult.message) else null
}