package edu.uco.rsteele5.gravityrunner

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.toolbar_user_profile.*

class UserProfile : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        setSupportActionBar(toolbar)
        init()
    }

    private fun init()
    {
        val toggle = ActionBarDrawerToggle(Activity(),drawer_root,toolbar,R.string.drawer_open,R.string.drawer_close)
        drawer_root.addDrawerListener(toggle)
        toggle.syncState()
        nav_user_profile_navigation_root.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        when(item.itemId)
        {
            R.id.level->{
            }
            R.id.score-> {
            }
            R.id.coin->{
            }
        }
        drawer_root.closeDrawer(GravityCompat.START)
        return true
    }
}
