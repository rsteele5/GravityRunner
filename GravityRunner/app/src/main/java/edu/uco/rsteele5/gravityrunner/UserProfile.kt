package edu.uco.rsteele5.gravityrunner

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.content_user_profile.*
import kotlinx.android.synthetic.main.toolbar_user_profile.*

class UserProfile : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        setSupportActionBar(toolbar)
        init()
        setTitle(getString(R.string.user_activity))
        var mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth?.currentUser
        if (currentUser != null){
            val headerView = nav_user_profile_navigation_root.getHeaderView(0)
            val userView = headerView.findViewById<TextView>(R.id.tName)
            val emailView = headerView.findViewById<TextView>(R.id.tEmail)
            val email = currentUser.email
            val index = email?.indexOf('@')
            if(index != null)
                userView.text = email.substring(0,index)
            emailView.text= email

            /*
            val headerView = nav_view.getHeaderView(0)
            val emailView = headerView.findViewById<TextView>(R.id.navEmail)
            emailView.text = currentUser.email
            */
        }else{
            Toast.makeText(this,getString(R.string.noUser),Toast.LENGTH_SHORT).show()
        }

        btnLevel.setOnClickListener {
            val i = Intent(this, LevelSelect::class.java)
            startActivity(i)
        }

        btnStore.setOnClickListener {
            //val i = Intent(this, Store::class.java)
            //            startActivity(i)
        }

        btnBoard.setOnClickListener {
            //val i = Intent(this, LeaderBoard::class.java)
            //            startActivity(i)
        }

        btnOut.setOnClickListener {
            mAuth?.signOut()
            if(mAuth.currentUser == null)
                Toast.makeText(this,getString(R.string.logout),Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this,getString(R.string.fail_logout),Toast.LENGTH_SHORT).show()
            finish()
        }

        btnQuit.setOnClickListener {
            mAuth?.signOut()
            if(mAuth.currentUser == null)
                Toast.makeText(this,getString(R.string.logout),Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this,getString(R.string.fail_logout),Toast.LENGTH_SHORT).show()
            finishAffinity()
        }
    }

    private fun init() {
        val toggle =
            ActionBarDrawerToggle(Activity(), drawer_root, toolbar, R.string.drawer_open, R.string.drawer_close)
        drawer_root.addDrawerListener(toggle)
        toggle.syncState()
        nav_user_profile_navigation_root.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        when (item.itemId) {
            R.id.level -> {
            }
            R.id.score -> {
            }
            R.id.coin -> {
            }
        }
        drawer_root.closeDrawer(GravityCompat.START)
        return true
    }
}
