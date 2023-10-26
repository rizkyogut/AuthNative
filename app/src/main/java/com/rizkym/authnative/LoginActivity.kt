package com.rizkym.authnative

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rizkym.authnative.databinding.ActivityLoginBinding
import com.rizkym.authnative.validator.EmailValidator
import com.rizkym.authnative.validator.EmptyValidator
import com.rizkym.authnative.validator.PasswordValidator
import com.rizkym.authnative.validator.base.BaseValidator
import com.rizkym.authnative.validator.base.ValidateResult

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
        database = Firebase.database.reference

        binding.btnLogin.setOnClickListener(this)
        binding.tvRegister.setOnClickListener(this)
        binding.tvForgotPassword.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnLogin -> {

                val email = binding.edEmail.text.toString()
                val password = binding.edPassword.text.toString()

                validation(email, password)
                prosesLogin(email, password)
            }
            binding.tvRegister -> intentToRegister()
            binding.tvForgotPassword -> intentToForgotPassword()
        }
    }

    private fun validation(email: String, password: String) {
        val emailValidations =
            BaseValidator.validate(EmptyValidator(email), EmailValidator(email))
        val passwordValidations =
            BaseValidator.validate(EmptyValidator(password), PasswordValidator(password))

        binding.tilEmail.error = messageValidation(emailValidations)
        binding.tilPassword.error = messageValidation(passwordValidations)
    }

    private fun prosesLogin(email: String, password: String) {
        if (binding.tilEmail.error == null && binding.tilPassword.error == null) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        updateUI()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }

    private fun updateUI() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun intentToForgotPassword() {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun intentToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun messageValidation(validateResult: ValidateResult) =
        if (!validateResult.isSuccess) getString(validateResult.message) else null

    companion object {
        private val TAG = LoginActivity::class.qualifiedName
    }
}