package com.example.dance

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Playlist(
    val id: String,
    val name: String,
    val description: String,
    val songs:@RawValue List<Song> = listOf()
) : Parcelable
