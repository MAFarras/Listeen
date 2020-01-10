package com.example.listeen.Admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listeen.R
import com.example.listeen.model.Song
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_edit_song.*
import java.util.*

class EditSongActivity : AppCompatActivity() {

    val AUDIO : Int = 2
    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_song)

        ref = FirebaseDatabase.getInstance().getReference("Song")

        val musicName = findViewById<TextView>(R.id.music_name)
        val file = findViewById<TextView>(R.id.url_music)
        musicName.text=intent.getStringExtra("name")
        file.text=intent.getStringExtra("file_music")

        songfile_add.setOnClickListener(View.OnClickListener {
                view: View? -> val intent = Intent()
            intent.setType ("audio/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Audio"), AUDIO)
        })

        edit_song.setOnClickListener {
            val musicName = music_name.text.toString()
            if (musicName.isEmpty()) {
                Toast.makeText(this, "Please Insert Song Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                if(selectedAudioUri==null){
                    saveSongToFirebaseDatabase(intent.getStringExtra("file_music"))

                }else{
                    uploadAudioToFirbaseStorage()
                }
            }
        }

    }

    var selectedAudioUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == AUDIO && resultCode== Activity.RESULT_OK && data != null){
            selectedAudioUri= data.data

            val fileMusic= selectedAudioUri.toString()

            url_music.setText(fileMusic)

            songfile_add.alpha=0f
//            val bitmapDrawable= BitmapDrawable(bitmap)
//            select_photo.setBackgroundDrawable(bitmapDrawable)
        }


    }

    private fun uploadAudioToFirbaseStorage(){
        if (selectedAudioUri == null) return
        val filename= UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/Song/$filename")

        ref.putFile(selectedAudioUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    val song=it.toString()
                    saveSongToFirebaseDatabase(song)
                }
            }

    }

    private fun saveSongToFirebaseDatabase(musicUrl: String){
        val id_album=intent.getStringExtra("id_album")
        val name_album=intent.getStringExtra("name_album")
        val id_singer=intent.getStringExtra("id_singer")
        val name_singer=intent.getStringExtra("name_singer")
        val name=music_name.text.toString()
        val uid= intent.getStringExtra("id")


        val ref = FirebaseDatabase.getInstance().getReference("/Song/")
        val song=
            Song(uid, name, musicUrl, id_album, name_album, id_singer, name_singer)

        ref.child(uid).setValue(song)
            .addOnSuccessListener {
                Toast.makeText(this,"Saved Succesfully", Toast.LENGTH_SHORT).show()
            }
    }

}