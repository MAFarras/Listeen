package com.example.listeen

import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import com.example.listeen.adapter_user.Adapter_Artist_User
import com.example.listeen.model.Artist
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_artist_list_user.*

class ArtistActivity : AppCompatActivity(){

    lateinit var ref : DatabaseReference
    lateinit var list : MutableList<Artist>
    lateinit var listView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_list_user)

        ref = FirebaseDatabase.getInstance().getReference("Artist")
        list = mutableListOf()
        listView = findViewById(R.id.artistViewTask)

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
                        val artist = h.getValue(Artist::class.java)
                        list.add(artist!!)
                    }
                    val adapter = Adapter_Artist_User(this@ArtistActivity,R.layout.artist_item_list_user,list)
                    listView.adapter = adapter
                }
            }
        })


    }
}