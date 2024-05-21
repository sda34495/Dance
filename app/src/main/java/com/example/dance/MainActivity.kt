// MainActivity.kt

package com.example.dance

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var songsRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = FirebaseDatabase.getInstance()
        songsRef = database.getReference("songs")
        auth = FirebaseAuth.getInstance()


//        val songs = listOf(
//            Song("1", "Epoq Lepidoptera", "https://commondatastorage.googleapis.com/codeskulptor-assets/Epoq-Lepidoptera.ogg"),
//            Song("2", "Collision 8-Bit", "https://commondatastorage.googleapis.com/codeskulptor-assets/Collision8-Bit.ogg"),
//            Song("3", "Week 7 Bounce", "https://commondatastorage.googleapis.com/codeskulptor-assets/week7-bounce.m4a"),
//            Song("4", "Evil Laugh", "https://commondatastorage.googleapis.com/codeskulptor-assets/Evillaugh.ogg"),
//            Song("5", "Ate a Pill", "https://commondatastorage.googleapis.com/codeskulptor-demos/pyman_assets/ateapill.ogg"),
//            Song("6", "Intro Music", "https://commondatastorage.googleapis.com/codeskulptor-demos/pyman_assets/intromusic.ogg"),
//            Song("7", "Race 1 Music", "https://commondatastorage.googleapis.com/codeskulptor-demos/riceracer_assets/music/race1.ogg"),
//            Song("8", "Win Music", "https://commondatastorage.googleapis.com/codeskulptor-demos/riceracer_assets/music/win.ogg"),
//            Song("9", "Start Music", "https://commondatastorage.googleapis.com/codeskulptor-demos/riceracer_assets/music/start.ogg"),
//            Song("10", "Descent Background Music", "https://codeskulptor-demos.commondatastorage.googleapis.com/descent/background%20music.mp3")
//        )
//        pushSongsToFirebase(songs)
//        getSongsFromFirebase()
        // Start the LoginActivity when the app opens

        val currentUserId = auth.currentUser?.uid
        var intent = Intent(this, LoginActivity::class.java)
        if (currentUserId != null) {
            intent = Intent(this, HomeActivity::class.java)

        }
        startActivity(intent)
        finish() // Finish current activity to prevent back navigation
    }

}