package com.example.dance

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaylistAdapter(
    private val context: Context,
    private val playlists: MutableList<Playlist>,
    private val songs: MutableList<Song> = mutableListOf(),
    private var isSearchMode: Boolean = false
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_PLAYLIST = 0
    private val VIEW_TYPE_SONG = 1
    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newPlaylists: List<Playlist>, newSongs: List<Song>? = null, isSearchMode: Boolean = false) {
        playlists.clear()
        playlists.addAll(newPlaylists)
        newSongs?.let {
            songs.clear()
            songs.addAll(it)
        }
        this.isSearchMode = isSearchMode
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (songs.isNotEmpty()) VIEW_TYPE_SONG else VIEW_TYPE_PLAYLIST
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_PLAYLIST) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_playlist, parent, false)
            PlaylistViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false)
            SongViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PlaylistViewHolder) {
            holder.bind(playlists[position])
        } else if (holder is SongViewHolder) {
            holder.bind(songs[position], true)
        }
    }

    override fun getItemCount(): Int {
        return if (songs.isNotEmpty()) songs.size else playlists.size
    }

    inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(playlist: Playlist) {
            itemView.findViewById<TextView>(R.id.playlistNameTextView).text = playlist.name
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(playlist)
            }
        }
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(song: Song, isSearchMode: Boolean) {
            itemView.findViewById<TextView>(R.id.songNameTextView).text = song.name
            val checkBox = itemView.findViewById<CheckBox>(R.id.songCheckbox)
            checkBox.visibility = if (isSearchMode) View.GONE else View.VISIBLE
            itemView.setOnClickListener {
                itemClickListener?.onSongClick(song)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(playlist: Playlist)
        fun onSongClick(song: Song)
    }
}
