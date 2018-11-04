package edu.uco.rsteele5.gravityrunner

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_gravity_runner_main.*

class GravityRunnerMain : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gravity_runner_main)

        id_playButton.setOnClickListener {
            val alert = AlertDialog.Builder(this)
            alert.setTitle(getString(R.string.beforePlay))
            alert.setMessage(getString(R.string.loginFirst))
            alert.setPositiveButton("Yes"){dialog: DialogInterface?, which: Int ->
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
            }
            alert.setNegativeButton("No"){dialog: DialogInterface?, which: Int ->
                val i = Intent(this, GameEngine::class.java)
                startActivity(i)
            }
            alert.show()
            //val i = Intent(this, GameEngine::class.java)
            //startActivity(i)
        }
    }
}
