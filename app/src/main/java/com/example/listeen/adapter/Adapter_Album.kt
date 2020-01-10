package com.example.listeen.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.listeen.Admin.EditAlbumActivity
import com.example.listeen.Admin.SongListActivity
import com.example.listeen.R
import com.example.listeen.model.Album
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class Adapter_Album (val mCtx: Context, val layoutResId: Int, val list: List<Album> )
    : ArrayAdapter<Album>(mCtx,layoutResId,list){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId,null)

        val albumTitle = view.findViewById<TextView>(R.id.nama_album)
        val albumImage = view.findViewById<ImageView>(R.id.image_album)

        val buttonUpdate = view.findViewById<Button>(R.id.edit_album)
        val buttonDelete = view.findViewById<Button>(R.id.delete_album)

        val album = list[position]
        val id_album=album.id
        albumTitle.text = album.albumName
        val url=album.picture
        Picasso.get().load(url).into(albumImage)

        buttonUpdate.setOnClickListener {
            val intent = Intent(it.context, EditAlbumActivity::class.java)
            intent.putExtra("id",album.id)
            intent.putExtra("name",album.albumName)
            intent.putExtra("image",album.picture)
            intent.putExtra("id_singer",album.id_singer)
            intent.putExtra("singer_name",album.singer_name)
            it.context.startActivity(intent)
        }

        buttonDelete.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("/Album/")
            val ref2 = FirebaseDatabase.getInstance().getReference("/Song/")

            ref.child(id_album).removeValue()
            ref2.orderByChild("id_album").equalTo(id_album).addChildEventListener(object :
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

        albumImage.setOnClickListener {
            val intent = Intent(it.context, SongListActivity::class.java)
            intent.putExtra("id_album",album.id)
            intent.putExtra("id_singer",album.id_singer)
            intent.putExtra("name_singer",album.singer_name)
            intent.putExtra("name_album",album.albumName)
            intent.putExtra("picture",album.picture)
            it.context.startActivity(intent)
        }

        Toast.makeText(mCtx,"Deleted Succesfully", Toast.LENGTH_SHORT).show()

        return view

    }
}