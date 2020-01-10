package com.example.listeen

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.listeen.adapter_user.Adapter_Song_User
import com.example.listeen.model.Song
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_song_list_user.*

class SongListActivityUser : AppCompatActivity() {

    lateinit var ref: DatabaseReference
    lateinit var list: MutableList<Song>
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_list_user)

        ref = FirebaseDatabase.getInstance().getReference("Song")
        list = mutableListOf()
        listView = findViewById(R.id.songViewTask)

        val id_filter = intent.getStringExtra("id_filter")
        val textTitle = findViewById<TextView>(R.id.title)

        if(id_filter=="1"){
            textTitle.setText(intent.getStringExtra("nama_artis"))
            val id_artist = intent.getStringExtra("id_artist")

            back.setOnClickListener {
                val intent = Intent(this, ArtistActivity::class.java)
                startActivity(intent)
                finish()
            }

            ref.orderByChild("id_singer").equalTo(id_artist).addValueEventListener(object :
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
                        val adapter = Adapter_Song_User(
                            this@SongListActivityUser,
                            R.layout.song_item_list_user, list
                        )
                        listView.adapter = adapter
                    }
                }
            })
        }else if(id_filter=="2"){
            textTitle.setText(intent.getStringExtra("nama_album"))
            val id_album = intent.getStringExtra("id_album")

            back.setOnClickListener {
                val intent = Intent(this, AlbumActivity::class.java)
                startActivity(intent)
                finish()
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
                        val adapter = Adapter_Song_User(
                            this@SongListActivityUser,
                            R.layout.song_item_list_user, list
                        )
                        listView.adapter = adapter
                    }
                }
            })
        }
    }
}