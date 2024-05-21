package com.example.dance


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlin.random.Random

class HomeActivity : AppCompatActivity() {

    private val artists = listOf(
        Artist(
            "Artist 1",
            "Emily Carter is a contemporary artist celebrated for her evocative and vibrant mixed-media works. Born in 1985 in Portland, Oregon, Emily's creative journey began at a young age, inspired by the lush landscapes and eclectic culture of the Pacific Northwest. She pursued formal training in fine arts, earning a BFA from the Rhode Island School of Design in 2007 and an MFA from the School of the Art Institute of Chicago in 2011.",
            "https://i.pinimg.com/550x/d1/79/c5/d179c5c424ed339058effcb85c3f0f49.jpg",
            "https://www.example.com/song1"
        ),
        Artist(
            "Artist 2",
            "Emily Carter is a contemporary artist celebrated for her evocative and vibrant mixed-media works. Born in 1985 in Portland, Oregon, Emily's creative journey began at a young age, inspired by the lush landscapes and eclectic culture of the Pacific Northwest. She pursued formal training in fine arts, earning a BFA from the Rhode Island School of Design in 2007 and an MFA from the School of the Art Institute of Chicago in 2011.",
            "https://i.pinimg.com/550x/d1/79/c5/d179c5c424ed339058effcb85c3f0f49.jpg",
            "https://www.example.com/song2"
        ),
        Artist(
            "Artist 3",
            "Emily Carter is a contemporary artist celebrated for her evocative and vibrant mixed-media works. Born in 1985 in Portland, Oregon, Emily's creative journey began at a young age, inspired by the lush landscapes and eclectic culture of the Pacific Northwest. She pursued formal training in fine arts, earning a BFA from the Rhode Island School of Design in 2007 and an MFA from the School of the Art Institute of Chicago in 2011.",
            "https://i.pinimg.com/550x/d1/79/c5/d179c5c424ed339058effcb85c3f0f49.jpg",
            "https://www.example.com/song3"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val welcomeMessage = findViewById<TextView>(R.id.welcome_message)
        welcomeMessage.text = "Welcome to Dance App!"

        val artist = artists.random()
       val options =   RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round);

        val artistImageView = findViewById<ImageView>(R.id.artist_image)
//        Glide.with(this).load(artist.imageUrl).into(artistImageView)


         print(artist.imageUrl)


        Glide.with(this).load(artist.imageUrl).apply(options).into(artistImageView)

        val artistBioTextView = findViewById<TextView>(R.id.artist_bio)
        artistBioTextView.text = artist.bio

        val songLinkButton = findViewById<Button>(R.id.song_link_button)
        songLinkButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(artist.songLink))
            startActivity(intent)
        }

        // Setup bottom navigation
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val homeButton = findViewById<Button>(R.id.nav_home)
        val playlistsButton = findViewById<Button>(R.id.nav_playlists)
        val searchButton = findViewById<Button>(R.id.nav_search)
        val profileButton = findViewById<Button>(R.id.nav_profile)

        homeButton.setOnClickListener {
            // Already on Home, do nothing
        }

        playlistsButton.setOnClickListener {
            // Navigate to Playlists
            val intent = Intent(this, PlaylistsActivity::class.java)
            startActivity(intent)
            finish()
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
}
