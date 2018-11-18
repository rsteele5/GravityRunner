package edu.uco.rsteele5.gravityrunner

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
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

        var score= ArrayList<Int>()
        var status= ArrayList<Int>()

        status.add(0)
        score.add(0)
        if (current != null) {
            getDataDB().execute()
        }
        else{
            Toast.makeText(this,"no current user",Toast.LENGTH_LONG).show()
        }

        //Thread.sleep(1000)
    }
    inner class getDataDB: AsyncTask<Int,Int,Int>(){
        override fun onPreExecute() {//main UI
        }

        override fun doInBackground(vararg params: Int?): Int {
            val mAuth = FirebaseAuth.getInstance()
            val db = FirebaseFirestore.getInstance()
            var current = mAuth.currentUser?.email

            var score=0
            var status= ArrayList<Int>()

            var docRef = db?.collection("$current/Levels/1")?.document("Properties")
            status.add(0)
            docRef?.get()?.addOnSuccessListener {
                var stat = it?.getData().toString()
                val index1 = stat.indexOf('=')
                val index2 = stat.indexOf(',')
                val index3 = stat.lastIndexOf('=')
                val index4 = stat.lastIndexOf('}')
                status.add(stat.substring(index1 + 1, index2).toInt())
                score = stat.substring(index3 + 1, index4).toInt()
            }?.addOnFailureListener{
                status.add(0)
            }
            Thread.sleep(2000)
            return score
        }

        override fun onPostExecute(result: Int?) {
            if(result != null) {
                levelList.add(Level("title", "bob", result, 0, 1))
                levelList.add(Level("title", "bob", 0, 0, 2))
                levelList.add(Level("title", "bob", 0, -1, 3))
                levelList.add(Level("title", "bob", 0, -1, 4))
                levelList.add(Level("title", "bob", 0, -1, 5))
            }

            rLevelView.adapter.notifyDataSetChanged()
        }
    }
}
