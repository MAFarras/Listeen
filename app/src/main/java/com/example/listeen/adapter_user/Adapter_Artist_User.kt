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
import com.example.listeen.model.Artist
import com.squareup.picasso.Picasso

class Adapter_Artist_User (val mCtx: Context, val layoutResId: Int, val list: List<Artist> )
    : ArrayAdapter<Artist>(mCtx,layoutResId,list){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId,null)

        val artistName = view.findViewById<TextView>(R.id.nama_artist)
        val artistImage = view.findViewById<ImageView>(R.id.image_artist)

        val artist = list[position]

        artistName.text = artist.artistName
        val url=artist.picture
        Picasso.get().load(url).into(artistImage)

        artistImage.setOnClickListener {
            val intent = Intent(it.context, SongListActivityUser::class.java)
            intent.putExtra("id_artist",artist.id)
            intent.putExtra("nama_artis",artist.artistName)
            intent.putExtra("id_filter","1")
            it.context.startActivity(intent)
        }

        return view

    }
}