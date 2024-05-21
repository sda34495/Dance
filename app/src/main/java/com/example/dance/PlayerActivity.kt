package com.example.dance

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class PlayerActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private var currentSongIndex: Int = 0
    private lateinit var songs: List<Song>
    private lateinit var playPauseButton: ImageButton
    private lateinit var songProgressBar: SeekBar
    private lateinit var songTitle: TextView
    private lateinit var songArtist: TextView

    private val handler = Handler(Looper.getMainLooper())

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        songs = intent.getParcelableArrayListExtra<Song>("SONGS_LIST") ?: emptyList()

        songTitle = findViewById(R.id.songTitle)
        songArtist = findViewById(R.id.songArtist)
        songProgressBar = findViewById(R.id.songProgressBar)
        playPauseButton = findViewById(R.id.playPauseButton)
        val nextButton = findViewById<ImageButton>(R.id.nextButton)
        val prevButton = findViewById<ImageButton>(R.id.prevButton)

        playPauseButton.setOnClickListener {
            if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                playPauseButton.setImageResource(R.drawable.ic_play)
            } else if (::mediaPlayer.isInitialized) {
                mediaPlayer.start()
                playPauseButton.setImageResource(R.drawable.ic_pause)
                updateProgressBar()
            }
        }

        nextButton.setOnClickListener {
            playNextSong()
        }

        prevButton.setOnClickListener {
            playPreviousSong()
        }

        val imageView = findViewById<ImageView>(R.id.imageAboveProgressBar)
        Glide.with(this)
            .load("https://t3.ftcdn.net/jpg/05/85/10/62/360_F_585106274_GbJNWuJ9gnj93G19sRT4eK54ojKysO0t.jpg")
            .into(imageView)

        songProgressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        playSong(songs[currentSongIndex].url)
        setupBottomNavigation()
    }

    private fun playSong(url: String) {
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepare()
            start()
        }

        songTitle.text = songs[currentSongIndex].name
        songArtist.text = songs[currentSongIndex].artist  // assuming Song class has artist property
        playPauseButton.setImageResource(R.drawable.ic_pause)

        mediaPlayer.setOnCompletionListener {
            playNextSong()
        }

        songProgressBar.max = mediaPlayer.duration
        updateProgressBar()
    }

    private fun playNextSong() {
        currentSongIndex = (currentSongIndex + 1) % songs.size
        playSong(songs[currentSongIndex].url)
    }

    private fun playPreviousSong() {
        currentSongIndex = if (currentSongIndex - 1 < 0) songs.size - 1 else currentSongIndex - 1
        playSong(songs[currentSongIndex].url)
    }

    private fun updateProgressBar() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
                    songProgressBar.progress = mediaPlayer.currentPosition
                }
                handler.postDelayed(this, 1000)
            }
        }, 1000)
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
            // Navigate to Profile
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
