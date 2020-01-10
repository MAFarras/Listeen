package com.example.listeen.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.listeen.Admin.AddArtist
import com.example.listeen.Admin.AlbumListActivity
import com.example.listeen.Admin.EditArtistActivity
import com.example.listeen.R
import com.example.listeen.model.Artist
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class Adapter_Artist (val mCtx: Context, val layoutResId: Int, val list: List<Artist> )
    : ArrayAdapter<Artist>(mCtx,layoutResId,list){


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId,null)

        val artistTitle = view.findViewById<TextView>(R.id.nama)
        val artistImage = view.findViewById<ImageView>(R.id.image_artist)

        val buttonUpdate = view.findViewById<Button>(R.id.edit_artist)
        val buttonDelete = view.findViewById<Button>(R.id.delete_artist)

        val artist = list[position]

        artistTitle.text = artist.artistName
        val artist_id=artist.id
        val url=artist.picture
        Picasso.get().load(url).into(artistImage)

        buttonUpdate.setOnClickListener {
            val intent = Intent(it.context, EditArtistActivity::class.java)
            intent.putExtra("id",artist.id)
            intent.putExtra("name",artist.artistName)
            intent.putExtra("image",artist.picture)
            it.context.startActivity(intent)
        }

        buttonDelete.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("/Artist/")
            val ref2 = FirebaseDatabase.getInstance().getReference("/Album/")
            val ref3 = FirebaseDatabase.getInstance().getReference("/Song/")

            ref.child(artist_id).removeValue()
            ref2.orderByChild("id_singer").equalTo(artist_id).addChildEventListener(object :
                ChildEventListener {
                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    p0.getRef().removeValue()
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                }

                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    p0.getRef().removeValue()
                }
            })
            ref3.orderByChild("id_singer").equalTo(artist_id).addChildEventListener(object :
                ChildEventListener {
                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    p0.getRef().removeValue()
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    p0.getRef().removeValue()
                }
            })
            Toast.makeText(mCtx,"Removed Succcesfully", Toast.LENGTH_SHORT).show();
        }

        artistImage.setOnClickListener {
            val intent = Intent(it.context, AlbumListActivity::class.java)
            intent.putExtra("id",artist.id)
            intent.putExtra("name",artist.artistName)
            intent.putExtra("image",artist.picture)
            it.context.startActivity(intent)
        }


        return view

    }
}