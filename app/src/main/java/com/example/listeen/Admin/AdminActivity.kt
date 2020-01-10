package com.example.listeen.Admin

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.listeen.R
import com.example.listeen.RegisterLogin.LoginActivity
import com.example.listeen.adapter.Adapter_Artist
import com.example.listeen.model.Artist
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    lateinit var ref : DatabaseReference
    lateinit var list : MutableList<Artist>
    lateinit var listView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        ref = FirebaseDatabase.getInstance().getReference("Artist")
        list = mutableListOf()
        listView = findViewById(R.id.listViewTask)

        toolbar = findViewById(R.id.toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.navigationView)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()){

                    list.clear()
                    for (h in p0.children){
                        val film = h.getValue(Artist::class.java)
                        list.add(film!!)
                    }
                    val adapter = Adapter_Artist(this@AdminActivity,R.layout.activity_artist_list,list)
                    listView.adapter = adapter
                }
            }
        })


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_artist -> {
                Toast.makeText(this, "Add Song", Toast.LENGTH_SHORT).show()
                val intent = Intent (this, AddArtist::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show()
                FirebaseAuth.getInstance().signOut()
                val intent = Intent (this, LoginActivity::class.java)
                intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                return true
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}