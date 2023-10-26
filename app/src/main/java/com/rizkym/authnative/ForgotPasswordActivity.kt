package com.rizkym.authnative

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rizkym.authnative.databinding.ActivityForgotPasswordBinding
import com.rizkym.authnative.validator.EmailValidator
import com.rizkym.authnative.validator.EmptyValidator
import com.rizkym.authnative.validator.base.BaseValidator

class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.btnForgotPassword.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnForgotPassword -> {

                val email = binding.edEmail.text.toString()
                validation(email)
                sendResetPassword(email)
            }
        }
    }

    private fun sendResetPassword(email: String) {
        if (binding.tilEmail.error == null) {

            auth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Check email to reset password", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Failed to reset password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validation(email: String) {
        val emailValidations = BaseValidator.validate(EmptyValidator(email), EmailValidator(email))
        binding.tilEmail.error = if (!emailValidations.isSuccess) getString(emailValidations.message) else null
    }

}