package edu.uco.rsteele5.gravityrunner

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.content_user_profile.*
import kotlinx.android.synthetic.main.toolbar_user_profile.*

class UserProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        setSupportActionBar(toolbar)
        //init()

        setTitle(getString(R.string.user_activity))

        val headerView = nav_user_profile_navigation_root.getHeaderView(0)
        val coinView = headerView.findViewById<TextView>(R.id.tCoin)
        val levelView = headerView.findViewById<TextView>(R.id.tLevel)
        val scoreView = headerView.findViewById<TextView>(R.id.tScore)

        var mAuth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        var score = 0
        var level = 1
        var coin = 0

        val currentUser = mAuth?.currentUser
        if (currentUser != null) {
            val userView = headerView.findViewById<TextView>(R.id.tName)
            val emailView = headerView.findViewById<TextView>(R.id.tEmail)
            val email = currentUser.email
            val index = email?.indexOf('@')
            if (index != null)
                userView.text = email.substring(0, index)
            emailView.text = email

            if (email != null) {
                userInfo().execute()
                //coin = userInfo().execute().get().toInt()
            } else {
                Toast.makeText(this, getString(R.string.noUser), Toast.LENGTH_SHORT).show()
            }

            levelView.text = getString(R.string.level, level)
            scoreView.text = getString(R.string.score, score)
            coinView.text = getString(R.string.coin, coin)


            btnLevel.setOnClickListener {
                val i = Intent(this, LevelSelect::class.java)
                startActivity(i)
            }

            btnStore.setOnClickListener {
                //val i = Intent(this, Store::class.java)
                //            startActivity(i)
            }

            btnOut.setOnClickListener {
                mAuth?.signOut()
                if (mAuth.currentUser == null)
                    Toast.makeText(this, getString(R.string.logout), Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, getString(R.string.fail_logout), Toast.LENGTH_SHORT).show()
                finish()
            }

            btnQuit.setOnClickListener {
                mAuth?.signOut()
                if (mAuth.currentUser == null)
                    Toast.makeText(this, getString(R.string.logout), Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, getString(R.string.fail_logout), Toast.LENGTH_SHORT).show()
                finishAffinity()
            }
        }
    }

    private fun init() {
        val toggle =
            ActionBarDrawerToggle(Activity(), drawer_root, toolbar, R.string.drawer_open, R.string.drawer_close)
        drawer_root.addDrawerListener(toggle)
        toggle.syncState()
    }

    inner class userInfo : AsyncTask<Int, Int, String>() {

        override fun doInBackground(vararg params: Int?): String {
            val mAuth = FirebaseAuth.getInstance()
            val db = FirebaseFirestore.getInstance()
            var email = mAuth.currentUser?.email
            var coin="a"
            if (email != null) {
                db?.collection(email)?.document("Coins")?.get()
                    ?.addOnSuccessListener {
                            var stat = it?.getData().toString()
                            val index1 = stat.indexOf('=')
                            val index2 = stat.lastIndexOf('}')
                            coin = stat.substring(startIndex = index1 + 1, endIndex = index2)//
                    }?.addOnFailureListener {
                        coin = "fail"
                    }
            }
            Thread.sleep(5000)
            return coin
        }
    }
}

