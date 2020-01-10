package com.example.listeen.adapter

import android.content.Context
import android.content.Intent

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.listeen.Admin.EditAlbumActivity
import com.example.listeen.Admin.EditSongActivity
import com.example.listeen.R
import com.example.listeen.model.Song
import com.google.firebase.database.FirebaseDatabase

class Adapter_Song (val mCtx: Context, val layoutResId: Int, val list: List<Song> )
    : ArrayAdapter<Song>(mCtx,layoutResId,list){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId,null)

        val songTitle = view.findViewById<TextView>(R.id.song_title)
//        val filmImage = view.findViewById<ImageView>(R.id.image_artist)

        val buttonPlay = view.findViewById<Button>(R.id.play_song)
        val buttonStop = view.findViewById<Button>(R.id.stop_song)

        val buttonDelete = view.findViewById<Button>(R.id.delete_song)
        val buttonEdit = view.findViewById<Button>(R.id.edit_song)

        val song = list[position]

        songTitle.text = song.songName

        val mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(song.file_music)

        buttonDelete.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("/Song/")
            ref.child(song.id).removeValue()
            Toast.makeText(mCtx,"Deleted Succesfully", Toast.LENGTH_SHORT).show()
        }

        buttonEdit.setOnClickListener {
            val intent = Intent(it.context, EditSongActivity::class.java)
            intent.putExtra("id",song.id)
            intent.putExtra("name",song.songName)
            intent.putExtra("id_singer",song.id_singer)
            intent.putExtra("name_singer",song.name_singer)
            intent.putExtra("id_album",song.id_album)
            intent.putExtra("name_album",song.name_album)
            intent.putExtra("file_music",song.file_music)
            it.context.startActivity(intent)
        }

        buttonPlay.setOnClickListener {
                mediaPlayer?.prepare()
                mediaPlayer?.start()
        }

        buttonStop.setOnClickListener {
            mediaPlayer?.stop()
        }


        return view

    }
}