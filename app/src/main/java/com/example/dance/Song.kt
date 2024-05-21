package com.example.dance

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val id: String,
    val name: String,
    val url: String,
    val artist: String
) : Parcelable
