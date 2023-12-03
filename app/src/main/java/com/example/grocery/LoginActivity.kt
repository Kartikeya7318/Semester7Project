package com.example.grocery

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var tvNavigateToRegister: TextView;

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Initialize Views
        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnRegister)
        tvNavigateToRegister = findViewById(R.id.tvNavigateToRegister)
        // Set onClickListener for Login Button
        btnLogin.setOnClickListener {
            // Call the login method
            login()
        }
        tvNavigateToRegister.setOnClickListener { navigateToRegisterActivity() }
        checkAuthentication();
    }
    private fun checkAuthentication() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // The user is already authenticated, navigate to the main screen or perform necessary actions.
            // For example, you can start the main activity.
            navigateToMainActivity()
        }
    }



    private fun login() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validate email and password
        if (validateForm(email, password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth.currentUser
                        Toast.makeText(
                            this@LoginActivity,
                            "Authentication successful.",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToMainActivity()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            this@LoginActivity,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Optional: Finish the current activity to prevent the user from navigating back to the registration screen
    }
    private fun navigateToRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish() // Optional: Finish the current activity to prevent the user from navigating back to the registration screen
    }

    private fun validateForm(email: String, password: String): Boolean {
        var valid = true

        if (email.isEmpty()) {
            tilEmail.error = "Email is required"
            valid = false
        } else {
            tilEmail.error = null
        }

        if (password.isEmpty()) {
            tilPassword.error = "Password is required"
            valid = false
        } else {
            tilPassword.error = null
        }

        return valid
    }
}
