package edu.uco.rsteele5.gravityrunner

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_level_select.*
import java.util.*

class LevelSelect : AppCompatActivity() {

    private var levelList = ArrayList<Level>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)
        title = getString(R.string.level_activity)

        rLevelView.layoutManager = LinearLayoutManager(this)
        rLevelView.adapter = LevelArrayAdapter(this, levelList)

        var db = FirebaseFirestore.getInstance()
        var mAuth = FirebaseAuth.getInstance()
        var current = mAuth.currentUser?.email


        for (i in 1..5) {
            var docRef = db?.collection("$current/Levels/$i")?.document("Properties")
            docRef?.get()
                ?.addOnSuccessListener {
                    var score = it?.getDouble("Score")
                    var status = it?.getDouble("Status")
                    val level = it.toObject(Level::class.java)
                    level?.id = it.id
                    if(score!=null && status != null)
                        levelList.add(Level("title", "bob", score.toInt(), status.toInt(), i))
                    rLevelView.adapter.notifyDataSetChanged()
                }
        }
    }
}
