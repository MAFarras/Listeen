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
import com.example.listeen.model.Artist
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_artist.*
import kotlinx.android.synthetic.main.activity_edit_artist.artist_name
import kotlinx.android.synthetic.main.activity_edit_artist.image_artist
import kotlinx.android.synthetic.main.activity_edit_artist.picture_artist
import java.util.*

class EditArtistActivity : AppCompatActivity() {
    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_artist)

        val artistName = findViewById<TextView>(R.id.artist_name)
        val editArtist=findViewById<Button>(R.id.edit_artist)
        val image=findViewById<ImageView>(R.id.image_artist)

        artistName.text=intent.getStringExtra("name")

        Picasso.get().load(intent.getStringExtra("image")).into(image)

        ref = FirebaseDatabase.getInstance().getReference("Artist")

        picture_artist.setOnClickListener{
            val intent= Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)
        }

        editArtist.setOnClickListener {
            val artistName = artist_name.text.toString()
            if (artistName.isEmpty()) {
                Toast.makeText(this, "Please Insert Artist Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                if(selectedPhotoUri==null){
                    saveArtistToFirebaseDatabase(intent.getStringExtra("image"))
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
        val artistId=intent.getStringExtra("id")
//        val uid= ref.push().key.toString()


        val ref = FirebaseDatabase.getInstance().getReference("/Artist/")
        val ref2 = FirebaseDatabase.getInstance().getReference("/Song/")
        val ref3 = FirebaseDatabase.getInstance().getReference("/Album/")
        val artist=
            Artist(artistId, name, imageUrl)

        ref2.orderByChild("id_singer").equalTo(artistId).addChildEventListener(object :
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
                p0.getRef().child("name_singer").setValue(name)
            }
        })
        ref3.orderByChild("id_singer").equalTo(artistId).addChildEventListener(object :
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
                p0.getRef().child("singer_name").setValue(name)
            }
        })

        ref.child(artistId).setValue(artist)
            .addOnSuccessListener {
                Toast.makeText(this,"Saved Succesfully", Toast.LENGTH_SHORT).show()
                val intent = Intent (this, AdminActivity::class.java)
                startActivity(intent)
                finish()
            }
    }
}