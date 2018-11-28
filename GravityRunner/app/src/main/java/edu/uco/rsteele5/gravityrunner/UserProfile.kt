package edu.uco.rsteele5.gravityrunner

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.content_user_profile.*

class UserProfile : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private val currentUser = mAuth?.currentUser
    var email: String? = null
    var docRefCoin: DocumentReference? = null
    var docRefCostume: DocumentReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_user_profile)

        title = getString(R.string.user_activity)

        if (currentUser != null) {
            email = currentUser.email
            val index = email?.indexOf('@')
            if (index != null)
                tName.text = email?.substring(0, index)
            tEmail.text = email

            docRefCoin = db.collection("$email").document("Coins")
            docRefCostume = db.collection("$email").document("Costumes")


            docRefCoin?.get()
                ?.addOnSuccessListener {
                    val stat = it?.getDouble("amount")?.toInt()
                    tCoin.text = "Coin: $stat"
                }
            docRefCostume?.get()?.addOnSuccessListener {
                tCostume.text = if (it.getDouble("Dragon")?.toInt() == 1)
                    "Costume: Dragon"
                else if (it.getDouble("Knight")?.toInt() == 1)
                    "Costume: Knight"
                else if (it.getDouble("Wizard")?.toInt() == 1)
                    "Costume: Wizard"
                else
                    "Costume: Nothing"
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

    override fun onResume() {
        super.onResume()

        docRefCoin?.get()
            ?.addOnSuccessListener {
                val stat = it?.getDouble("amount")?.toInt()
                tCoin.text = "Coin: $stat"
            }
        docRefCostume?.get()?.addOnSuccessListener {
            tCostume.text = if (it.getDouble("Dragon")?.toInt() == 1)
                "Costume: Dragon"
            else if (it.getDouble("Knight")?.toInt() == 1)
                "Costume: Knight"
            else if (it.getDouble("Wizard")?.toInt() == 1)
                "Costume: Wizard"
            else
                "Costume: Nothing"
        }
    }
}


