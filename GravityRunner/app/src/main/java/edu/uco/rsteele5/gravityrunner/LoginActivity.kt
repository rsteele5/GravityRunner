package edu.uco.rsteele5.gravityrunner

import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*



const val TAG_LA = "LOGINACTIVITY"

class LoginActivity : Activity() {

    private var mAuth:FirebaseAuth? = null
    private var db: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        btnLogIn.setOnClickListener {
            var email = eAccount.text.toString().trim()
            var password = ePass.text.toString().trim()
            if(email.isNullOrEmpty() || password.isNullOrEmpty())
            {
                Toast.makeText(this,getString(R.string.emptyAccount),Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mAuth?.signInWithEmailAndPassword(email,password)
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, getString(R.string.loginSuccess, email), Toast.LENGTH_SHORT).show()

                        //var i = Intent(this,GameEngine::class.java)
                        val i = Intent(this, UserProfile::class.java)
                        //intent.putExtra(EMAIL,email)  //player's information / current user fun
                        //intent.putExtra(PASSWORD,password)

                        startActivity(i)
                        eAccount.text = null
                        ePass.text = null
                    } else {
                        Toast.makeText(this, getString(R.string.loginFail), Toast.LENGTH_SHORT).show()
                    }
                }
        }

        btnCreate.setOnClickListener {
            var email = eAccount.text.toString().trim()
            var password = ePass.text.toString().trim()
            if(password.length<6){
                Toast.makeText(this,getString(R.string.short_password),Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            db = FirebaseFirestore.getInstance()
            var amount: HashMap<String, Any> = HashMap()
            var costume: HashMap<String, Any> = HashMap()
            var level: HashMap<String, Any> = HashMap()
            var level1: HashMap<String, Any> = HashMap()
            amount.put("amount",0)
            costume.put("Drgon",-1)
            costume.put("Knight",-1)
            costume.put("Wizard",-1)
            costume.put("Equipped","nothing")
            level.put("Score",0)
            level.put("Status",-1)
            level1.put("Score",0)
            level1.put("Status",0)

            mAuth?.createUserWithEmailAndPassword(email,password)
                ?.addOnSuccessListener {
                    Toast.makeText(this,getString(R.string.successCreate),Toast.LENGTH_SHORT).show()
                    db?.collection(email)?.document("Coins")?.set(amount)
                    db?.collection(email)?.document("Costumes")?.set(costume)
                    db?.collection(email)?.document("Levels")?.collection("1")?.document("Properties")?.set(level1)
                    for(i in 2..5)
                        db?.collection(email)?.document("Levels")?.collection("$i")?.document("Properties")
                            ?.set(level)

                }
                ?.addOnFailureListener {
                    Toast.makeText(this,it.toString(),Toast.LENGTH_LONG).show()
                    Log.d(TAG_LA,it.toString())
                }
        }
    }

}
