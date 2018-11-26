package edu.uco.rsteele5.gravityrunner

import android.app.Activity
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_level_select.*
import java.util.*
import android.content.Intent
import android.util.Log


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
                    var score = ArrayList<Int>()
                    var stat = it?.getData().toString()
                    val index1 = stat.indexOf('=')
                    val index2 = stat.indexOf(',')
                    val index3 = stat.lastIndexOf('=')
                    val index4 = stat.lastIndexOf('}')
                    score.add(stat.substring(index3 + 1, index4).toInt())
                    score.add(stat.substring(index1 + 1, index2).toInt())
                    val level = it.toObject(Level::class.java)
                    level?.id = it.id
                    levelList.add(Level("title", "bob", score[0], score[1], i))
                    rLevelView.adapter.notifyDataSetChanged()
                }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        val levelDataArray = ArrayList<LevelData>()
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                levelDataArray.addAll(data.getParcelableArrayListExtra(LEVEL))
            }
        }
        for(levelData in levelDataArray){
            /*
             * levelData.level TODO: get the properties of the completed level
             * If the level is locked, unlock it.
             *
             * if(levelData.score > levelHighScore) TODO: if the score is higher than the current high score
             *      TODO: make this score the high score
             *
             * levelData.coins TODO: Add to players current total coins
             */
            Log.d(LEVEL,"--->$levelData")
        }
    }
}
