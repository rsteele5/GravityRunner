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
        setContentView(R.layout.content_user_profile)
        setTitle(getString(R.string.user_activity))

        var mAuth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        val currentUser = mAuth?.currentUser
        if (currentUser != null) {
            val email = currentUser.email
            val index = email?.indexOf('@')
            if (index != null)
                tName.text = email.substring(0, index)
            tEmail.text = email


            var docRefCoin = db?.collection("$email")?.document("Coins")
            var docRefCostume = db?.collection("$email")?.document("Costumes")
            docRefCoin?.get()
                ?.addOnSuccessListener {
                    //var score = ArrayList<Int>()
                    var stat = it?.getDouble("amount")?.toInt()
                    tCoin.text = "Coin: $stat"
                }
                    docRefCostume?.get()?.addOnSuccessListener {
                        var stat1 = it.getDouble("Dragon")?.toInt()
                        var stat2 = it.getDouble("Knight")?.toInt()
                        var stat3 = it.getDouble("Wizard")?.toInt()

                        if(stat1==1)
                            tCostume.text = "Costume: Dragon"
                        else if(stat2 == 1)
                            tCostume.text = "Costume: Knight"
                        else if(stat3 == 1)
                            tCostume.text = "Costume: Wizard"
                        else
                            tCostume.text = "Costume: Nothing"
                    }
            }

            btnLevel.setOnClickListener {
                val i = Intent(this, LevelSelect::class.java)
                startActivity(i)
            }

            btnStore.setOnClickListener {
                val i = Intent(this, StoreActivity::class.java)
                startActivity(i)
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


