package com.example.dance

import SongAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.concurrent.atomic.AtomicInteger

class SearchActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var adapter: SongAdapter
    private val allSongs = mutableListOf<Song>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        database = FirebaseDatabase.getInstance().reference.child("songs")

        val recyclerView = findViewById<RecyclerView>(R.id.searchRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SongAdapter(this, mutableListOf())
        recyclerView.adapter = adapter

        fetchSongsFromFirebase()
        setupBottomNavigation()

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterSongs(newText)
                return true
            }
        })
    }

    private fun fetchSongsFromFirebase() {
        Log.d("Firebase", "Fetching songs from Firebase")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                allSongs.clear()
                Log.d("Firebase", "DataSnapshot received: ${dataSnapshot.exists()}")
                if (dataSnapshot.exists()) {
                    for (songSnapshot in dataSnapshot.children) {
                        val id = songSnapshot.key ?: ""
                        val name = songSnapshot.child("name").getValue(String::class.java) ?: ""
                        val url = songSnapshot.child("url").getValue(String::class.java) ?: ""
                        val artist = songSnapshot.child("artist").getValue(String::class.java) ?: ""
                        val song = Song(id, name, url,artist)
                        allSongs.add(song)
                    }
                    adapter.updateData(allSongs)
                } else {
                    Log.d("Firebase", "No songs found in the database.")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Failed to read songs.", databaseError.toException())
            }
        })
    }

    private fun filterSongs(query: String?) {
        val filteredSongs = if (!query.isNullOrEmpty()) {
            allSongs.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.artist.contains(query, ignoreCase = true)

            }
        } else {
            allSongs
        }
        adapter.updateData(filteredSongs);
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
            // Already on Search screen, do nothing
        }

        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
