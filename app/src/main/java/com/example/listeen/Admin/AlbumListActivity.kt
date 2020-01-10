package com.example.listeen.Admin

import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.listeen.R
import com.example.listeen.adapter.Adapter_Album
import com.example.listeen.model.Album
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_addalbum.*

class AlbumListActivity : AppCompatActivity() {

    lateinit var ref : DatabaseReference
    lateinit var list : MutableList<Album>
    lateinit var listView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_list)

        ref = FirebaseDatabase.getInstance().getReference("Album")
        list = mutableListOf()
        listView = findViewById(R.id.listViewTask)

        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")
        val textNama = findViewById<TextView>(R.id.nama_artis)
        val artistImage = findViewById<ImageView>(R.id.image_artist)

        textNama.setText(intent.getStringExtra("name"))
        Picasso.get().load(intent.getStringExtra("image")).into(artistImage)

        add_album.setOnClickListener {
            val intent = Intent(it.context, AddAlbum::class.java)
            intent.putExtra("id",id)
            intent.putExtra("name",name)
            it.context.startActivity(intent)
        }

        ref.orderByChild("id_singer").equalTo(id).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()){

                    list.clear()
                    for (h in p0.children){
                        val album = h.getValue(Album::class.java)
                        list.add(album!!)
                    }
                    val adapter = Adapter_Album(this@AlbumListActivity,R.layout.album_item_list,list)
                    listView.adapter = adapter
                }
            }
        })


    }
}