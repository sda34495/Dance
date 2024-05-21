package com.example.dance

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.concurrent.atomic.AtomicInteger

//class PlaylistsActivity : AppCompatActivity(), PlaylistAdapter.OnItemClickListener {
//
//    private lateinit var auth: FirebaseAuth
//    private lateinit var database: DatabaseReference
//    private lateinit var adapter: PlaylistAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_playlists)
//
//        auth = FirebaseAuth.getInstance()
//        database = FirebaseDatabase.getInstance().reference.child("playlists")
//
//        val recyclerView = findViewById<RecyclerView>(R.id.playlistRecyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        adapter = PlaylistAdapter(this, mutableListOf())
//        adapter.setOnItemClickListener(this)
//        recyclerView.adapter = adapter
//
//        fetchPlaylistsFromFirebase()
//        setupBottomNavigation()
//
//        // Find the "Create Playlist" button
//        val createPlaylistButton = findViewById<Button>(R.id.createPlaylistButton)
//
//        // Set OnClickListener to the button
//        createPlaylistButton.setOnClickListener {
//            // Start the process to create a new playlist
//            // For example, show a dialog or navigate to a new activity
//            val dialogFragment = PlaylistDialogFragment(object : PlaylistDialogFragment.OnPlaylistCreatedListener {
//                override fun onPlaylistCreated(playlistName: String, songs: List<Song>) {
//                    // Implement the logic to create a new playlist here
//                    // For now, you can just print the playlist name and songs
//                    println("New Playlist: $playlistName")
//                    songs.forEach {
//                        println("Song: ${it.name}, URL: ${it.url}")
//                    }
//                }
//            })
//            dialogFragment.show(supportFragmentManager, "PlaylistDialogFragment")
//        }
//    }
//
//
//    private fun fetchPlaylistsFromFirebase() {
//        Log.d("Firebase", "Fetching playlists from Firebase")
//        val currentUserId = auth.currentUser?.uid
//        if (currentUserId == null) {
//            Log.e("Firebase", "User not logged in.")
//            return
//        }
//        database.orderByChild("userId").equalTo(currentUserId).addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val playlists = mutableListOf<Playlist>()
//                Log.d("Firebase", "DataSnapshot received: ${dataSnapshot.exists()}")
//                if (dataSnapshot.exists()) {
//                    for (playlistSnapshot in dataSnapshot.children) {
//                        val id = playlistSnapshot.child("id").getValue(String::class.java) ?: ""
//                        val name = playlistSnapshot.child("name").getValue(String::class.java) ?: ""
//                        val songIds = playlistSnapshot.child("songs").children.map { it.getValue(String::class.java) ?: "" }
//                        getSongsDetails(songIds) { songs ->
//                            val playlist = Playlist(id, name, "Wow what a game.",songs)
//                            playlists.add(playlist)
//                            // Update adapter only after fetching all song details
//                            if (playlists.size == dataSnapshot.childrenCount.toInt()) {
//                                adapter.updateData(playlists)
//                            }
//                        }
//                    }
//                } else {
//                    Log.d("Firebase", "No playlists found in the database.")
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.e("Firebase", "Failed to read playlists.", databaseError.toException())
//            }
//        })
//    }
//    private fun getSongsDetails(songIds: List<String>, callback: (List<Song>) -> Unit) {
//        val songsRef = FirebaseDatabase.getInstance().reference.child("songs")
//        val songs = mutableListOf<Song>()
//        val remainingCalls = AtomicInteger(songIds.size)
//
//        Log.d("Firebase", "Fetching details for song IDs: $songIds")
//        for (songId in songIds) {
//            Log.d("Firebase", "Fetching details for song ID: $songId")
//            songsRef.orderByChild("id").equalTo(songId).addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    for (data in dataSnapshot.children) {
//                        val id = data.child("id").getValue(String::class.java) ?: ""
//                        val name = data.child("name").getValue(String::class.java) ?: ""
//                        val url = data.child("url").getValue(String::class.java) ?: ""
//                        val song = Song(id, name, url)
//                        songs.add(song)
//
//                        Log.d("Firebase", "Retrieved song: $song")
//
//                        // Decrement the counter and check if all data has been retrieved
//                        if (remainingCalls.decrementAndGet() == 0) {
//                            Log.d("Firebase", "All song details retrieved: $songs")
//                            callback(songs)
//                        }
//                    }
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    Log.e("Firebase", "Failed to read song details for song ID: $songId", databaseError.toException())
//                    // Even on failure, decrement the counter
//                    if (remainingCalls.decrementAndGet() == 0) {
//                        callback(songs)
//                    }
//                }
//            })
//        }
//    }
//
//
//
//
//
//
//    private fun setupBottomNavigation() {
//        val homeButton = findViewById<Button>(R.id.navHome)
//        val playlistsButton = findViewById<Button>(R.id.navPlaylists)
//        val searchButton = findViewById<Button>(R.id.navSearch)
//        val profileButton = findViewById<Button>(R.id.navProfile)
//
//        homeButton.setOnClickListener {
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//
//        playlistsButton.setOnClickListener {
//            // Already on Playlists screen, do nothing
//        }
//
//        searchButton.setOnClickListener {
//            // Navigate to Search
//        }
//
//        profileButton.setOnClickListener {
//            // Navigate to Profile
//        }
//    }
//
//    override fun onItemClick(playlist: Playlist) {
//        Log.d("playerDetail",playlist.toString())
//        val intent = Intent(this, PlayerActivity::class.java)
//        intent.putExtra("playlistId", playlist.id)
//        intent.putParcelableArrayListExtra("SONGS_LIST", ArrayList(playlist.songs))
//        startActivity(intent)
//    }
//}

class PlaylistsActivity : AppCompatActivity(), PlaylistAdapter.OnItemClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var adapter: PlaylistAdapter
    private val allPlaylists = mutableListOf<Playlist>()
    private val allSongs = mutableListOf<Song>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlists)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("playlists")

        val recyclerView = findViewById<RecyclerView>(R.id.playlistRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PlaylistAdapter(this, mutableListOf())
        adapter.setOnItemClickListener(this)
        recyclerView.adapter = adapter

        fetchPlaylistsFromFirebase()
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

        val createPlaylistButton = findViewById<Button>(R.id.createPlaylistButton)
        createPlaylistButton.setOnClickListener {
            val dialogFragment = PlaylistDialogFragment(object : PlaylistDialogFragment.OnPlaylistCreatedListener {
                override fun onPlaylistCreated(playlistName: String, songs: List<Song>) {
                    println("New Playlist: $playlistName")
                    songs.forEach {
                        println("Song: ${it.name}, URL: ${it.url}")
                    }
                }
            })
            dialogFragment.show(supportFragmentManager, "PlaylistDialogFragment")
        }
    }

    private fun fetchPlaylistsFromFirebase() {
        Log.d("Firebase", "Fetching playlists from Firebase")
        val currentUserId = auth.currentUser?.uid
        if (currentUserId == null) {
            Log.e("Firebase", "User not logged in.")
            return
        }
        database.orderByChild("userId").equalTo(currentUserId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                allPlaylists.clear()
                allSongs.clear()
                val songIdsSet = HashSet<String>()
                val playlists = mutableListOf<Playlist>()
                Log.d("Firebase", "DataSnapshot received: ${dataSnapshot.exists()}")
                if (dataSnapshot.exists()) {
                    for (playlistSnapshot in dataSnapshot.children) {
                        val id = playlistSnapshot.key ?: ""
                        val name = playlistSnapshot.child("name").getValue(String::class.java) ?: ""
                        val songIds = playlistSnapshot.child("songs").children.map { it.getValue(String::class.java) ?: "" }
                        getSongsDetails(songIds) { songs ->
                            val uniqueSongs = songs.filter { songIdsSet.add(it.id) }
                            val playlist = Playlist(id, name, "Wow what a game.", uniqueSongs)
                            playlists.add(playlist)
                            allPlaylists.add(playlist)
                            allSongs.addAll(uniqueSongs)
                            if (playlists.size == dataSnapshot.childrenCount.toInt()) {
                                adapter.updateData(playlists)
                            }
                        }
                    }
                } else {
                    Log.d("Firebase", "No playlists found in the database.")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Failed to read playlists.", databaseError.toException())
            }
        })
    }


    private fun getSongsDetails(songIds: List<String>, callback: (List<Song>) -> Unit) {
        val songsRef = FirebaseDatabase.getInstance().reference.child("songs")
        val songs = mutableListOf<Song>()
        val remainingCalls = AtomicInteger(songIds.size)

        Log.d("Firebase", "Fetching details for song IDs: $songIds")
        for (songId in songIds) {
            Log.d("Firebase", "Fetching details for song ID: $songId")
            songsRef.orderByChild("id").equalTo(songId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        val id = data.child("id").getValue(String::class.java) ?: ""
                        val name = data.child("name").getValue(String::class.java) ?: ""
                        val url = data.child("url").getValue(String::class.java) ?: ""
                        val artist = data.child("artist").getValue(String::class.java) ?: ""
                        val song = Song(id, name, url,artist)
                        songs.add(song)

                        Log.d("Firebase", "Retrieved song: $song")

                        if (remainingCalls.decrementAndGet() == 0) {
                            Log.d("Firebase", "All song details retrieved: $songs")
                            callback(songs)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("Firebase", "Failed to read song details for song ID: $songId", databaseError.toException())
                    if (remainingCalls.decrementAndGet() == 0) {
                        callback(songs)
                    }
                }
            })
        }
    }

    private fun filterSongs(query: String?) {
        val filteredSongs = if (!query.isNullOrEmpty()) {
            allSongs.filter { it.name.startsWith(query, ignoreCase = true) }
        } else {
            allSongs
        }
        adapter.updateData(emptyList(), filteredSongs,true)

        // Notify the adapter about the changes
        adapter.notifyDataSetChanged()
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
            // Already on Playlists screen, do nothing
        }

        searchButton.setOnClickListener {
            // Navigate to Search
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        profileButton.setOnClickListener {
            // Navigate to Profile
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onItemClick(playlist: Playlist) {
        Log.d("playerDetail", playlist.toString())
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra("playlistId", playlist.id)
        intent.putParcelableArrayListExtra("SONGS_LIST", ArrayList(playlist.songs))
        startActivity(intent)
    }

    override fun onSongClick(song: Song) {
        Log.d("songDetail", song.toString())
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putParcelableArrayListExtra("SONGS_LIST", ArrayList(listOf(song)))
        startActivity(intent)
    }
}
