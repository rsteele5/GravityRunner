package edu.uco.rsteele5.gravityrunner

import android.app.Activity
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

    private val levelList = ArrayList<Level>()
    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private val current = mAuth.currentUser?.email
    private var addedCoins = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)
        title = getString(R.string.level_activity)

        rLevelView.layoutManager = LinearLayoutManager(this)
        rLevelView.adapter = LevelArrayAdapter(this, levelList)

        val levelName = R.string.level1-1
        for (i in 1..5) {
            val docRef = db.collection("$current/Levels/$i").document("Properties")
            docRef.get().addOnSuccessListener {
                val score = it?.getDouble("Score")
                val status = it?.getDouble("Status")
                if(status != null && score  != null) {
                    levelList.add(Level(getString(levelName + i),
                            "background$i", score.toLong(), status.toInt(), i))
                    levelList.sortBy { lev -> lev.level }
                    rLevelView.adapter.notifyDataSetChanged()
                }
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        //Gather the data from the GameEngine
        val levelDataArray = ArrayList<LevelData>()
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK)
                levelDataArray.addAll(data.getParcelableArrayListExtra(LEVEL))
        }
        //Update the levelList
        for (levelData in levelDataArray) {
            //set the score
            if (levelList[levelData.level-1].score < levelData.score)
                levelList[levelData.level-1].score = levelData.score
            //set the status
            levelList[levelData.level-1].status = 1
            //Set the status of the next level
            if (levelData.level < 5 && levelList[levelData.level].status < 0)
                levelList[levelData.level].status = 0
            //Add the coins to total earned
            addedCoins += levelData.coins
        }
        rLevelView.adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        rLevelView.adapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        //Update levels on Firebase
        for (level in levelList) {
            val docRef = db.collection("$current/Levels/${level.level}").document("Properties")
            docRef.get().addOnSuccessListener { it ->
                val currentScore = it.getDouble("Score")?.toLong()
                val status = it.getDouble("Status")?.toInt()
                if(status != null){
                    if(status != level.status)
                        docRef.update("Status", level.status)
                    if(level.status == 1){
                        if (currentScore != null) {
                            if (currentScore < level.score)
                                docRef.update("Score", level.score)
                        }
                    }
                }
            }
        }
        //Update Coins
        val coinRef = db.collection("$current").document("Coins")
        coinRef.get().addOnSuccessListener {
            var totalCoins = it.getDouble("amount")?.toLong()
            if (totalCoins != null) {
                totalCoins += addedCoins
                coinRef.update("amount",totalCoins)
            }
        }
    }
}
