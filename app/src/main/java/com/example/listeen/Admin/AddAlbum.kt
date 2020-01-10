package com.example.listeen.Admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.example.listeen.R
import com.example.listeen.model.Album
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_addalbum.*
import java.util.*

class AddAlbum : AppCompatActivity() {

    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addalbum)

        ref = FirebaseDatabase.getInstance().getReference("Album")

        picture_album.setOnClickListener{
            val intent=Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)
        }

        add_album.setOnClickListener {
            val albumName = album_name.text.toString()
            if (albumName.isEmpty()) {
                Toast.makeText(this, "Please Insert Album Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                uploadImageToFirbaseStorage()
            }
        }


    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && resultCode== Activity.RESULT_OK && data != null){
            selectedPhotoUri= data.data

            val bitmap= MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            image_album.setImageBitmap(bitmap)

            picture_album.alpha=0f
        }


    }

    private fun uploadImageToFirbaseStorage(){
        if (selectedPhotoUri == null) return
        val filename= UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/Album/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    val image=it.toString()
                    saveAlbumToFirebaseDatabase(image)
                }
            }

    }

    private fun saveAlbumToFirebaseDatabase(imageUrl: String){
        val id_artist=intent.getStringExtra("id")
        val artist_name=intent.getStringExtra("name")
        val name=album_name.text.toString()
        val uid= ref.push().key.toString()


        val ref = FirebaseDatabase.getInstance().getReference("/Album/")
        val album=
            Album(uid, name, imageUrl, id_artist,artist_name)

        ref.child(uid).setValue(album)
            .addOnSuccessListener {
                Toast.makeText(this,"Saved Succesfully",Toast.LENGTH_SHORT).show()
            }
    }

}