// MyApplication.kt
package com.example.dance

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        println("Initializing APP.............................................")
        FirebaseApp.initializeApp(this)
    }
}
