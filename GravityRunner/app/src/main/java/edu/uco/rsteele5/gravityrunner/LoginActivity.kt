package edu.uco.rsteele5.gravityrunner

import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_login.*

const val TAG_LA = "LOGINACTIVITY"
//const val EMAIL = "email"
//const val PASSWORD = "password"

class LoginActivity : Activity() {

    private var mAuth:FirebaseAuth? = null

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

                        var i = Intent(this,GameEngine::class.java)

                        //intent.putExtra(EMAIL,email)  //player's information / current user fun
                        //intent.putExtra(PASSWORD,password)

                        startActivity(i)
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
            mAuth?.createUserWithEmailAndPassword(email,password)
                ?.addOnSuccessListener {
                    Toast.makeText(this,getString(R.string.successCreate),Toast.LENGTH_SHORT).show()
                }
                ?.addOnFailureListener {
                    Toast.makeText(this,it.toString(),Toast.LENGTH_LONG).show()
                    Log.d(TAG_LA,it.toString())
                }
        }
    }

}
