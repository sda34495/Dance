//package com.example.dance
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.MenuItem
//import android.widget.Button
//import android.widget.EditText
//import androidx.appcompat.app.AppCompatActivity
//
//class RegisterStep1Activity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register_step1)
//
//        val submitButton = findViewById<Button>(R.id.submit_button)
//
//        submitButton.setOnClickListener {
//            // Gather user inputs from the form
//            val email = findViewById<EditText>(R.id.email).text.toString()
//            val password = findViewById<EditText>(R.id.password).text.toString()
//            val repassword = findViewById<EditText>(R.id.repassword).text.toString()
//            val fullName = findViewById<EditText>(R.id.full_name).text.toString()
//            val dateOfBirth = findViewById<EditText>(R.id.date_of_birth).text.toString()
//            val phone = findViewById<EditText>(R.id.phone).text.toString()
//
//            // You might want to validate the inputs here
//
//            // Start the next activity
//            val intent = Intent(this, RegisterStep2Activity::class.java)
//            startActivity(intent)
//        }
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle back navigation
//        if (item.itemId == android.R.id.home) {
//            onBackPressed()
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
//}

package com.example.dance

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class RegisterStep1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_step1)

        val submitButton = findViewById<Button>(R.id.submit_button)

        submitButton.setOnClickListener {
            // Gather user inputs from the form
            val email = findViewById<EditText>(R.id.email).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            val repassword = findViewById<EditText>(R.id.repassword).text.toString()
            val fullName = findViewById<EditText>(R.id.full_name).text.toString()
            val dateOfBirth = findViewById<EditText>(R.id.date_of_birth).text.toString()
            val phone = findViewById<EditText>(R.id.phone).text.toString()

            // Validate the inputs here (optional)
            if (password != repassword) {
                // Show error message
                // e.g., Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Start the next activity with the collected data
            val intent = Intent(this, RegisterStep2Activity::class.java).apply {
                putExtra("email", email)
                putExtra("password", password)
                putExtra("fullName", fullName)
                putExtra("dateOfBirth", dateOfBirth)
                putExtra("phone", phone)
            }
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle back navigation
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
