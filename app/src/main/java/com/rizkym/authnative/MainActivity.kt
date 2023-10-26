package com.rizkym.authnative

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rizkym.authnative.databinding.ActivityMainBinding
import com.rizkym.authnative.model.User

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        val query = database.child("users").child(auth.uid.toString())
        addUserEventListener(query)

        binding.btnVerfied.setOnClickListener(this)
        binding.btnSignOut.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()

        checkEmailVerificationStatus(auth.currentUser)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnVerfied -> emailVerification(auth.currentUser)
            binding.btnSignOut -> signOut()
        }
    }

    private fun addUserEventListener(userReference: DatabaseReference) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val user = dataSnapshot.getValue(User::class.java)
                binding.apply {
                    tvName.text = user?.username
                    tvEmail.text = user?.email
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        userReference.addValueEventListener(userListener)
    }

    private fun checkEmailVerificationStatus(currentUser: FirebaseUser?) {
        currentUser?.reload()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val isEmailVerified = currentUser.isEmailVerified
                if (isEmailVerified) {
                    // Email is verified, do something
                    binding.tvVerification.text = getString(R.string.email_verified)
                    binding.btnVerfied.visibility = View.GONE
                } else {
                    // Email is not verified, handle it (e.g., show a message)
                    binding.tvVerification.text = getString(R.string.email_not_verified)
                    binding.btnVerfied.visibility = View.VISIBLE
                }
            } else {
                // Error occurred while reloading the user
            }
        }
    }

    private fun emailVerification(user: FirebaseUser?) {
        user?.sendEmailVerification()?.addOnSuccessListener {
            Toast.makeText(this, "Check email to verified account", Toast.LENGTH_SHORT).show()
        }?.addOnFailureListener { e ->
            Log.e(TAG, e.toString())
        }
    }

    private fun signOut() {
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    companion object {
        private val TAG = MainActivity::class.qualifiedName
    }
}