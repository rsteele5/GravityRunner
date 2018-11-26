package edu.uco.rsteele5.gravityrunner

import android.app.Activity
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
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
                    var score = it?.getDouble("Score")
                    var status = it?.getDouble("Status")
                    val level = it.toObject(Level::class.java)
                    level?.id = it.id
                    if(score!=null && status != null)
                        levelList.add(Level("Level $i", "bob", score.toInt(), status.toInt(), i))
                    rLevelView.adapter.notifyDataSetChanged()
                }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        val levelDataArray = ArrayList<LevelData>()
        val db = FirebaseFirestore.getInstance()
        val mAuth = FirebaseAuth.getInstance()
        val current = mAuth.currentUser?.email
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                levelDataArray.addAll(data.getParcelableArrayListExtra(LEVEL))
            }
        }
        for(levelData in levelDataArray){
            var currentLevel = levelData.level
            val docRef =  db?.collection("$current/Levels/$currentLevel")?.document("Properties")
            val docRef2 =  db?.collection("$current/Levels/${currentLevel+1}")?.document("Properties")
            val coinRef = db?.collection("$current")?.document("Coins")
            docRef2.get().addOnSuccessListener {
                var stat = it.getDouble("Status")?.toInt()
                if (stat != null) {
                    if (currentLevel < 5 || stat != 1) {

                        docRef.update("Status", 1)
                        docRef2.update("Status", 0)
                    } else
                        docRef.update("Status", 1)
                }
            }
            docRef.get().addOnSuccessListener {
                var currentScore = it.getDouble("Score")
                if (currentScore != null) {
                    if(currentScore<levelData.score)
                    {
                        docRef.update("Score",levelData.score)
                    }
                }

            }

            coinRef.get().addOnSuccessListener {
                var currentCoin = it.getDouble("amount")
                var earnedCoin = levelData.coins
                if (currentCoin != null) {
                    coinRef.update("amount",currentCoin+earnedCoin)
                }
            }
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

    override fun onResume() {
        super.onResume()
        var db = FirebaseFirestore.getInstance()
        var mAuth = FirebaseAuth.getInstance()
        var current = mAuth.currentUser?.email


        levelList.clear()
        for (i in 1..5) {
            var docRef = db?.collection("$current/Levels/$i")?.document("Properties")
            docRef?.get()
                ?.addOnSuccessListener {
                    var score = it?.getDouble("Score")
                    var status = it?.getDouble("Status")
                    val level = it.toObject(Level::class.java)
                    level?.id = it.id
                    if(score!=null && status != null)
                        levelList.add(Level("Level $i", "bob", score.toInt(), status.toInt(), i))
                    rLevelView.adapter.notifyDataSetChanged()
                }
        }
    }
}
