package com.example.listeen

import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import com.example.listeen.Admin.AddAlbum
import com.example.listeen.adapter_user.Adapter_Album_User
import com.example.listeen.model.Album
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_album_list_user.*

class AlbumActivity : AppCompatActivity(){

    lateinit var ref : DatabaseReference
    lateinit var list : MutableList<Album>
    lateinit var listView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_list_user)

        ref = FirebaseDatabase.getInstance().getReference("Album")
        list = mutableListOf()
        listView = findViewById(R.id.albumViewTask)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        ref.addValueEventListener(object : ValueEventListener {
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
                    val adapter = Adapter_Album_User(this@AlbumActivity,R.layout.album_item_list_user,list)
                    listView.adapter = adapter
                }
            }
        })


    }
}