package com.example.listeen.adapter_user

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.listeen.R
import com.example.listeen.model.Song
import com.google.firebase.database.*

class Adapter_Song_User (val mCtx: Context, val layoutResId: Int, val list: List<Song> )
    : ArrayAdapter<Song>(mCtx,layoutResId,list){

    lateinit var ref : DatabaseReference

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId,null)

        val songTitle = view.findViewById<TextView>(R.id.song_title)
        val songAlbum = view.findViewById<TextView>(R.id.song_album)
        val songArtist = view.findViewById<TextView>(R.id.song_artist)

        val buttonPlay = view.findViewById<Button>(R.id.play_song)
        val buttonStop = view.findViewById<Button>(R.id.stop_song)

        val song = list[position]

        songTitle.text = song.songName
        songAlbum.text = song.name_album
        songArtist.text = song.name_singer

        val mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(song.file_music)


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