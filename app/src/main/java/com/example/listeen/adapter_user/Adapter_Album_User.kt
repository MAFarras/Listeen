package com.example.listeen.adapter_user

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.listeen.R
import com.example.listeen.SongListActivityUser
import com.example.listeen.model.Album
import com.squareup.picasso.Picasso

class Adapter_Album_User (val mCtx: Context, val layoutResId: Int, val list: List<Album> )
    : ArrayAdapter<Album>(mCtx,layoutResId,list){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId,null)

        val albumName = view.findViewById<TextView>(R.id.nama_album)
        val albumArtist = view.findViewById<TextView>(R.id.nama_artis_album)
        val albumImage = view.findViewById<ImageView>(R.id.image_album)

        val album = list[position]

        albumName.text = album.albumName
        albumArtist.text = album.singer_name
        val url=album.picture
        Picasso.get().load(url).into(albumImage)

        albumImage.setOnClickListener {
            val intent = Intent(it.context, SongListActivityUser::class.java)
            intent.putExtra("id_album",album.id)
            intent.putExtra("nama_album",album.albumName)
            intent.putExtra("id_filter","2")
            it.context.startActivity(intent)
        }

        return view

    }
}