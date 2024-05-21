package com.example.dance

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterStep2Activity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_step2)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val saveButton = findViewById<Button>(R.id.save_button)
        val skipButton = findViewById<Button>(R.id.skip_button)

        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        val fullName = intent.getStringExtra("fullName")
        val dateOfBirth = intent.getStringExtra("dateOfBirth")
        val phone = intent.getStringExtra("phone")

        saveButton.setOnClickListener {
            // Gather additional user inputs from the form
            val cardName = findViewById<EditText>(R.id.card_name).text.toString()
            val cardNumber = findViewById<EditText>(R.id.card_number).text.toString()
            val expiryDate = findViewById<EditText>(R.id.expiry_date).text.toString()
            val securityCode = findViewById<EditText>(R.id.security_code).text.toString()
            val zipCode = findViewById<EditText>(R.id.zip_code).text.toString()

            // Create user with additional information
            registerUser(email, password, fullName, dateOfBirth, phone, cardName, cardNumber, expiryDate, securityCode, zipCode)
        }

        skipButton.setOnClickListener {
            // Create user without additional information
            registerUser(email, password, fullName, dateOfBirth, phone, "", "", "", "", "")
        }
    }

    private fun registerUser(
        email: String?,
        password: String?,
        fullName: String?,
        dateOfBirth: String?,
        phone: String?,
        cardName: String,
        cardNumber: String,
        expiryDate: String,
        securityCode: String,
        zipCode: String
    ) {
        if (email != null && password != null) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            // Save user information in the Firebase Database
                            val user = User(userId, fullName ?: "", email, dateOfBirth ?: "", phone ?: "", cardName, cardNumber, expiryDate, securityCode, zipCode)
                            database.reference.child("users").child(userId).setValue(user)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // Sign in the user
                                        signInUser(email, password)
                                    } else {
                                        Toast.makeText(this, "Failed to save user data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    } else {
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registration and login successful", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
