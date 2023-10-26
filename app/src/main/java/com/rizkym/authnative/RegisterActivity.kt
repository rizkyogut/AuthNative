package com.rizkym.authnative

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rizkym.authnative.databinding.ActivityRegisterBinding
import com.rizkym.authnative.model.User
import com.rizkym.authnative.validator.ConfirmPasswordValidator
import com.rizkym.authnative.validator.EmailValidator
import com.rizkym.authnative.validator.EmptyValidator
import com.rizkym.authnative.validator.NameValidator
import com.rizkym.authnative.validator.PasswordValidator
import com.rizkym.authnative.validator.base.BaseValidator
import com.rizkym.authnative.validator.base.ValidateResult

class RegisterActivity : AppCompatActivity(), View.OnClickListener  {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
        database = Firebase.database.reference

        binding.btnRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnRegister -> {
                val name = binding.edName.text.toString()
                val email = binding.edEmail.text.toString()
                val password = binding.edPassword.text.toString()
                val confirmPassword = binding.edConfirmPassword.text.toString()

                validation(name, email, password, confirmPassword)
                registerAccount(email, password, name)
            }
        }
    }

    private fun registerAccount(email: String, password: String, name: String) {
        if (binding.tilName.error == null && binding.tilEmail.error == null &&
            binding.tilPassword.error == null && binding.tilConfirmPassword.error == null
        ) {

            //Sign Up
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser

                        createdUserToDatabase(name, email, user)
                        emailVerification(user)
                        updateUI()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Create User failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }

    private fun validation(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        val userValidation =
            BaseValidator.validate(EmptyValidator(name), NameValidator(name))
        val emailValidations =
            BaseValidator.validate(EmptyValidator(email), EmailValidator(email))
        val passwordValidations =
            BaseValidator.validate(EmptyValidator(password), PasswordValidator(password))
        val passwordConfirmValidations =
            BaseValidator.validate(
                EmptyValidator(confirmPassword),
                ConfirmPasswordValidator(password, confirmPassword)
            )

        binding.tilName.error = messageValidation(userValidation)
        binding.tilEmail.error = messageValidation(emailValidations)
        binding.tilPassword.error = messageValidation(passwordValidations)
        binding.tilConfirmPassword.error = messageValidation(passwordConfirmValidations)
    }

    private fun emailVerification(user: FirebaseUser?) {
        user?.sendEmailVerification()?.addOnSuccessListener {
            Toast.makeText(this, "Check email to verified account", Toast.LENGTH_SHORT).show()
        }?.addOnFailureListener { e ->
            Log.e(TAG, e.toString())
        }
    }

    private fun createdUserToDatabase(
        name: String,
        email: String,
        user: FirebaseUser?
    ) {
        val userData = User(name, email)
        user?.let {
            database.child("users").child(it.uid).setValue(userData)
                .addOnSuccessListener {
                    // Write was successful!
                    Log.d(TAG, "createUserToDatabase:success")
                }
                .addOnFailureListener {
                    // Write failed
                    Log.d(TAG, "createUserToDatabase:failed")
                }
            // [END rtdb_write_new_user_task]
        }
    }


    private fun messageValidation(validateResult: ValidateResult) =
        if (!validateResult.isSuccess) getString(validateResult.message) else null

    private fun updateUI() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private val TAG = RegisterActivity::class.qualifiedName
    }


}