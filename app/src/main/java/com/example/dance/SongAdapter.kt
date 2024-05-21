import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.tools.build.jetifier.core.utils.Log
import com.example.dance.PlayerActivity
import com.example.dance.R
import com.example.dance.Song

class SongAdapter(
    private val context: Context,
    private val songs: MutableList<Song>
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private val selectedSongs = mutableSetOf<Song>()
    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    fun updateData(newSongs: List<Song>) {
        songs.clear()
        songs.addAll(newSongs)
        notifyDataSetChanged()
    }

    fun getSelectedSongs(): List<Song> {
        return selectedSongs.toList()
    }

    fun toggleSelection(position: Int) {
        val song = songs[position]
        if (selectedSongs.contains(song)) {
            selectedSongs.remove(song)
        } else {
            selectedSongs.add(song)
        }
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song, selectedSongs.contains(song))
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val songNameTextView: TextView = itemView.findViewById(R.id.songNameTextView)
        private val checkBox: CheckBox = itemView.findViewById(R.id.songCheckbox)

        fun bind(song: Song, isSelected: Boolean) {
            songNameTextView.text = song.name
            checkBox.setOnCheckedChangeListener(null) // Remove previous listener to avoid unwanted calls
            checkBox.isChecked = isSelected
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                toggleSelection(adapterPosition)
            }
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}

//class SongAdapter(
//    private val context: Context,
//    private val songs: MutableList<Song>,
//    private var isSearchMode: Boolean = false
//) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
//
//    private val selectedSongs = mutableSetOf<Song>()
//    private var itemClickListener: OnItemClickListener? = null
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//        itemClickListener = listener
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun updateData(newSongs: List<Song>, isSearchMode: Boolean = false) {
//        songs.clear()
//        songs.addAll(newSongs)
//        this.isSearchMode = isSearchMode
//        notifyDataSetChanged()
//    }
//
//    fun getSelectedSongs(): List<Song> {
//        return selectedSongs.toList()
//    }
//
//    fun toggleSelection(position: Int) {
//        val song = songs[position]
//        if (selectedSongs.contains(song)) {
//            selectedSongs.remove(song)
//        } else {
//            selectedSongs.add(song)
//        }
//        notifyItemChanged(position)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false)
//        return SongViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
//        val song = songs[position]
//        holder.bind(song, selectedSongs.contains(song))
//    }
//
//    override fun getItemCount(): Int {
//        return songs.size
//    }
//
//    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(song: Song, isSelected: Boolean) {
//            itemView.findViewById<TextView>(R.id.songNameTextView).text = song.name
//            val checkBox = itemView.findViewById<CheckBox>(R.id.songCheckbox)
//            checkBox.isChecked = isSelected
//            checkBox.setOnCheckedChangeListener(null) // Remove previous listener to avoid unwanted calls
//            checkBox.isChecked = isSelected
//            checkBox.setOnCheckedChangeListener { _, isChecked ->
//                toggleSelection(adapterPosition)
//            }
////            checkBox.visibility = if (isSearchMode) View.GONE else View.VISIBLE
//
//            // Set click listener to open player activity only in search mode
//            itemView.setOnClickListener {
//                if(isSearchMode){
//                    val intent = Intent(context, PlayerActivity::class.java)
//                    intent.putParcelableArrayListExtra("SONGS_LIST", ArrayList(listOf(song)))
//                    context.startActivity(intent)
//                } else {
//                    itemClickListener?.onItemClick(adapterPosition)
//                }
//            }
//        }
//    }
//
//
//    interface OnItemClickListener {
//        fun onItemClick(position: Int)
//    }
//}
