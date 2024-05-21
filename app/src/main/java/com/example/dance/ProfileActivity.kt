package com.example.dance

import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.tools.build.jetifier.core.utils.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

class ProfileActivity : AppCompatActivity() {

    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var userPhoneTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        userNameTextView = findViewById(R.id.userNameTextView)
        userEmailTextView = findViewById(R.id.userEmailTextView)
        userPhoneTextView = findViewById(R.id.userPhoneTextView)
        logoutButton = findViewById(R.id.logoutButton)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {


            userNameTextView.setText(user.displayName)
            userEmailTextView.setText(user.email)
            userPhoneTextView.setText(user.phoneNumber)

//            fetchUserData(user)
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            navigateToLogin()
        }

        logoutButton.setOnClickListener {
            logout()
        }

        setupBottomNavigation()
    }

    private fun fetchUserData(user: FirebaseUser) {
        Log.d("profile",user.toString())


    }

    private fun logout() {
        auth.signOut()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupBottomNavigation() {
        val homeButton = findViewById<Button>(R.id.navHome)
        val playlistsButton = findViewById<Button>(R.id.navPlaylists)
        val searchButton = findViewById<Button>(R.id.navSearch)
        val profileButton = findViewById<Button>(R.id.navProfile)

        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        playlistsButton.setOnClickListener {
            val intent = Intent(this, PlaylistsActivity::class.java)
            startActivity(intent)
            finish()
        }

        searchButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        profileButton.setOnClickListener {
            // Stay in Profile Activity
        }
    }
}
