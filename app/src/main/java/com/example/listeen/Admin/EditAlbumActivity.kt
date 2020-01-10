package com.example.listeen.Admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listeen.R
import com.example.listeen.model.Album
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_addalbum.*
import java.util.*

class EditAlbumActivity : AppCompatActivity() {
    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_album)

        val albumName = findViewById<TextView>(R.id.album_name)
        val editArtist=findViewById<Button>(R.id.edit_album)
        val image=findViewById<ImageView>(R.id.image_album)

        albumName.text=intent.getStringExtra("name")

        Picasso.get().load(intent.getStringExtra("image")).into(image)

        picture_album.setOnClickListener{
            val intent= Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)
        }

        editArtist.setOnClickListener {
            val albumName = album_name.text.toString()
            if (albumName.isEmpty()) {
                Toast.makeText(this, "Please Insert Album Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                if(selectedPhotoUri==null){
                    saveAlbumToFirebaseDatabase(intent.getStringExtra("image"))
                }else{
                    uploadImageToFirbaseStorage()
                }
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
        val id_artist=intent.getStringExtra("id_singer")
        val artist_name=intent.getStringExtra("singer_name")
        val name=album_name.text.toString()
        val uid= intent.getStringExtra("id")


        val ref2 = FirebaseDatabase.getInstance().getReference("/Song/")
        val ref = FirebaseDatabase.getInstance().getReference("/Album/")
        val album=
            Album(uid, name, imageUrl, id_artist,artist_name)

        ref2.orderByChild("id_album").equalTo(uid).addChildEventListener(object :
            ChildEventListener {
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                p0.getRef().child("name_album").setValue(name)
            }
        })

        ref.child(uid).setValue(album)
            .addOnSuccessListener {
                Toast.makeText(this,"Saved Succesfully", Toast.LENGTH_SHORT).show()
            }
    }

}