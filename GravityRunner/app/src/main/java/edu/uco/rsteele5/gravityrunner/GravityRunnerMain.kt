package edu.uco.rsteele5.gravityrunner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_gravity_runner_main.*

class GravityRunnerMain : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gravity_runner_main)

        id_playButton.setOnClickListener {
            val i = Intent(this, GameEngine::class.java)
            startActivity(i)
        }
    }
}
