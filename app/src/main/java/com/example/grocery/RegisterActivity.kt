package com.example.grocery
// RegisterActivity.kt
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val etName: EditText = findViewById(R.id.etName)
        val etPhoneNumber: EditText = findViewById(R.id.etPhoneNumber)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val etConfirmPassword: EditText = findViewById(R.id.etConfirmPassword)
        val btnRegister: Button = findViewById(R.id.btnRegister)
        val tvNavigateToLogin: TextView = findViewById(R.id.tvNavigateToLogin)

        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val phoneNumber = etPhoneNumber.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (password == confirmPassword) {
                registerUser(email, password, name, phoneNumber)
            } else {
                // Handle password mismatch
                // You can show an error message or toast
                Toast.makeText(
                    this,
                    "Confirm Password is not same as the password",
                    Toast.LENGTH_SHORT
                ).show();
            }
        }
        tvNavigateToLogin.setOnClickListener { navigateToLoginActivity() }
    }

    private fun registerUser(email: String, password: String, name: String, phoneNumber: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    val user = auth.currentUser
                    saveUserDataToFirestore(user?.uid, name, phoneNumber, email)

                    // You can navigate to the main screen or do other actions
                } else {
                    // If registration fails, display a message to the user.
                    // You can check task.exception for more details.
                    // Handle the error, show a toast, etc.
                    Toast.makeText(this, "User Creation Auth Exception", Toast.LENGTH_SHORT).show();

                }
            }
    }

    private fun saveUserDataToFirestore(
        userId: String?,
        name: String,
        phoneNumber: String,
        email: String
    ) {
        val user = hashMapOf(
            "name" to name,
            "phone_number" to phoneNumber,
            "email" to email
        )

        userId?.let {
            firestore.collection("Profiles")
                .document(it)
                .set(user)
                .addOnSuccessListener {
                    // User data saved to Firestore successfully
                    Toast.makeText(this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                    navigateToLoginActivity()
                }
                .addOnFailureListener { e ->
                    // Handle the error, show a toast, etc.
                    Toast.makeText(this, "User Creation Error " + e.message, Toast.LENGTH_SHORT)
                        .show();
                }
        }
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Optional: Finish the current activity to prevent the user from navigating back to the registration screen
    }
}
