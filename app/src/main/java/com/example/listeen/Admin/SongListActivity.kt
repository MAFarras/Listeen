package com.example.listeen.Admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.listeen.R
import com.example.listeen.adapter.Adapter_Song
import com.example.listeen.model.Song
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_song_list.*

class SongListActivity : AppCompatActivity() {

    lateinit var ref: DatabaseReference
    lateinit var list: MutableList<Song>
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_list)

        ref = FirebaseDatabase.getInstance().getReference("Song")
        list = mutableListOf()
        listView = findViewById(R.id.listViewTask)

        val id_album = intent.getStringExtra("id_album")
        val name_album = intent.getStringExtra("name_album")
        val id_singer = intent.getStringExtra("id_singer")
        val name_singer = intent.getStringExtra("name_singer")
        val textNama = findViewById<TextView>(R.id.nama_album)
        val albumImage = findViewById<ImageView>(R.id.image_album)

        textNama.setText(intent.getStringExtra("name_album"))
        Picasso.get().load(intent.getStringExtra("picture")).into(albumImage)

        add_song.setOnClickListener {
            val intent = Intent(it.context, AddSongActivity::class.java)
            intent.putExtra("id_album", id_album)
            intent.putExtra("name_album", name_album)
            intent.putExtra("id_singer", id_singer)
            intent.putExtra("name_singer", name_singer)
            it.context.startActivity(intent)
        }

        ref.orderByChild("id_album").equalTo(id_album).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()) {

                    list.clear()
                    for (h in p0.children) {
                        val song = h.getValue(Song::class.java)
                        list.add(song!!)
                    }
                    val adapter = Adapter_Song(
                        this@SongListActivity,
                        R.layout.song_item_list, list
                    )
                    listView.adapter = adapter
                }
            }
        })


    }
}