package com.example.dance

import SongAdapter
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.util.Log

class PlaylistDialogFragment(private val onPlaylistCreatedListener: OnPlaylistCreatedListener) : DialogFragment(), SongAdapter.OnItemClickListener {

    private lateinit var adapter: SongAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_create_playlist, null)

        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.songRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SongAdapter(requireContext(), mutableListOf())
        adapter.setOnItemClickListener(this)
        recyclerView.adapter = adapter

        // Initialize Firebase database and auth
        database = FirebaseDatabase.getInstance().reference.child("songs")
        auth = FirebaseAuth.getInstance()

        // Fetch songs from Firebase
        getSongsFromFirebase()

        builder.setView(dialogView)
            .setTitle("Create Playlist")
            .setPositiveButton("Create") { _, _ ->
                // Create playlist with selected songs
                val selectedSongs = adapter.getSelectedSongs()
                val playlistName = dialogView.findViewById<EditText>(R.id.playlistNameEditText).text.toString()
                val userId = auth.currentUser?.uid ?: ""
                Log.d("PlaylistDialogFragment", "Selected songs: $selectedSongs")
                savePlaylistToFirebase(userId, playlistName, selectedSongs)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        return builder.create()
    }

    private fun getSongsFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val songs = mutableListOf<Song>()
                for (songSnapshot in dataSnapshot.children) {
                    val id = songSnapshot.child("id").getValue(String::class.java) ?: ""
                    val name = songSnapshot.child("name").getValue(String::class.java) ?: ""
                    val url = songSnapshot.child("url").getValue(String::class.java) ?: ""
                    val artist = songSnapshot.child("url").getValue(String::class.java) ?: ""
                    val song = Song(id, name, url,artist)
                    songs.add(song)
                }
                adapter.updateData(songs)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun savePlaylistToFirebase(userId: String, playlistName: String, selectedSongs: List<Song>) {
        val playlistRef = FirebaseDatabase.getInstance().reference.child("playlists").push()
        val playlist = mapOf(
            "userId" to userId,
            "name" to playlistName,
            "songs" to selectedSongs.map { it.id }
        )
        Log.d("PlaylistDialogFragment", "Saving playlist: $playlist")
        playlistRef.setValue(playlist).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("PlaylistDialogFragment", "Playlist saved successfully")
                onPlaylistCreatedListener.onPlaylistCreated(playlistName, selectedSongs)
            } else {
                Log.e("PlaylistDialogFragment", "Failed to save playlist", task.exception)
            }
        }
    }

    override fun onItemClick(position: Int) {
        // Handle item click
        adapter.toggleSelection(position)
    }

    interface OnPlaylistCreatedListener {
        fun onPlaylistCreated(playlistName: String, songs: List<Song>)
    }
}
