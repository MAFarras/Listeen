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
import com.example.listeen.model.Artist
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_artist.*
import kotlinx.android.synthetic.main.activity_addalbum.*
import java.util.*

class AddArtist : AppCompatActivity() {

    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_artist)

        ref = FirebaseDatabase.getInstance().getReference("Artist")

        picture_artist.setOnClickListener{
            val intent= Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)
        }

        add_artist.setOnClickListener {
            val artistName = artist_name.text.toString()
            if (artistName.isEmpty()) {
                Toast.makeText(this, "Please Insert Artist Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                uploadImageToFirbaseStorage()
                val intent = Intent (this, AdminActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && resultCode== Activity.RESULT_OK && data != null){
            selectedPhotoUri= data.data

            val bitmap= MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            image_artist.setImageBitmap(bitmap)

            picture_artist.alpha=0f
//            val bitmapDrawable= BitmapDrawable(bitmap)
//            select_photo.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun uploadImageToFirbaseStorage(){
        if (selectedPhotoUri == null) return
        val filename= UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/Artist/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    val image=it.toString()
                    saveArtistToFirebaseDatabase(image)
                }
            }

    }

    private fun saveArtistToFirebaseDatabase(imageUrl: String){
        val name=artist_name.text.toString()
        val uid= ref.push().key.toString()


        val ref = FirebaseDatabase.getInstance().getReference("/Artist/")
        val artist=
            Artist(uid, name, imageUrl)

        ref.child(uid).setValue(artist)
            .addOnSuccessListener {
                Toast.makeText(this,"Saved Succesfully", Toast.LENGTH_SHORT).show()
            }
    }

}