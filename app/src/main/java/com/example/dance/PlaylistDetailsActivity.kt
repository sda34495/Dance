package com.example.dance

import SongAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class PlaylistDetailsActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var adapter:SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_details)

        database = FirebaseDatabase.getInstance().reference.child("songs")

        val recyclerView = findViewById<RecyclerView>(R.id.songRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SongAdapter(this, mutableListOf())
        recyclerView.adapter = adapter

        val playlistId = intent.getStringExtra("playlistId")
        playlistId?.let { fetchSongsFromPlaylist(it) }
    }

    private fun fetchSongsFromPlaylist(playlistId: String) {
        database.orderByChild("playlistId").equalTo(playlistId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val songs = mutableListOf<Song>()
                    for (songSnapshot in dataSnapshot.children) {
                        val song = songSnapshot.getValue(Song::class.java)
                        song?.let {
                            songs.add(it)
                        }
                    }
                    adapter.updateData(songs)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
    }
}
