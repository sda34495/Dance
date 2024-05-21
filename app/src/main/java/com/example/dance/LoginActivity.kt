//package com.example.dance
//
//// LoginActivity.kt
//
//
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import androidx.appcompat.app.AppCompatActivity
//import com.google.android.material.snackbar.Snackbar
//
//class LoginActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//
//        val loginButton = findViewById<Button>(R.id.login_button)
//        val signUpLink = findViewById<Button>(R.id.sign_up_link)
//
//        loginButton.setOnClickListener {
//            // Handle login logic here
//            val email = findViewById<EditText>(R.id.email).text.toString()
//            val password = findViewById<EditText>(R.id.password).text.toString()
//
//            // Dummy validation, replace with actual validation
//            if (email.isNotEmpty() && password.isNotEmpty()) {
//                // Start the next activity on successful login
//                    val intent = Intent(this, HomeActivity::class.java)
//                    startActivity(intent)
//                    finish() // Finish current activity to prevent back navigation
//            } else {
//                Snackbar.make(it, "Please enter valid email and password", Snackbar.LENGTH_SHORT).show()
//            }
//        }
//
//        signUpLink.setOnClickListener {
//            // Start the registration process when the sign-up link is clicked
//            val intent = Intent(this, RegisterStep1Activity::class.java)
//            startActivity(intent)
//        }
//    }
//}

package com.example.dance

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
//        FirebaseApp.initializeApp(this)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val loginButton = findViewById<Button>(R.id.login_button)
        val signUpLink = findViewById<Button>(R.id.sign_up_link)

        loginButton.setOnClickListener {
            // Handle login logic here
            val email = findViewById<EditText>(R.id.email).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()

            // Validate email and password
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Perform Firebase login
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Login success, start HomeActivity
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish() // Finish current activity to prevent back navigation
                        } else {
                            // If login fails, display a message to the user
                            Snackbar.make(it, "Authentication Failed: ${task.exception?.message}", Snackbar.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Snackbar.make(it, "Please enter valid email and password", Snackbar.LENGTH_SHORT).show()
            }
        }

        signUpLink.setOnClickListener {
            // Start the registration process when the sign-up link is clicked
            val intent = Intent(this, RegisterStep1Activity::class.java)
            startActivity(intent)
        }
    }
}
