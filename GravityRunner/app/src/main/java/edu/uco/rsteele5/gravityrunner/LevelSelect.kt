package edu.uco.rsteele5.gravityrunner

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_level_select.*
import java.util.*

class LevelSelect : AppCompatActivity() {

    private var levelList = ArrayList<Level>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)
        setTitle(getString(R.string.level_activity))

        rLevelView.layoutManager = LinearLayoutManager(this)
        rLevelView.adapter = LevelArrayAdapter(this, levelList)

        var db = FirebaseFirestore.getInstance()
        var mAuth = FirebaseAuth.getInstance()
        var current = mAuth.currentUser?.email

        var score= ArrayList<Int>()
        var status= ArrayList<Int>()

        if (current != null) {
            var docRef = db?.collection("$current/Levels/1")?.document("Properties")

            score.add(0)
            status.add(0)
            docRef?.get()?.addOnSuccessListener {
                var stat = it?.getData().toString()
                val index1 = stat.indexOf('=')
                val index2 = stat.indexOf(',')
                val index3 = stat.lastIndexOf('=')
                val index4 = stat.lastIndexOf('}')
                score.add(0)
                status.add(0)
                score.add(stat.substring(index1 + 1, index2).toInt())
                status.add(stat.substring(index3 + 1, index4).toInt())
            }
                ?.addOnFailureListener{
                    score.add(0)
                    status.add(0)
                Toast.makeText(this,"failed",Toast.LENGTH_LONG).show()
            }

               /* ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    var document = it?.getResult()
                    var stat = document?.getData().toString()
                    val index1 = stat.indexOf('=')
                    val index2 = stat.indexOf(',')
                    val index3 = stat.lastIndexOf('=')
                    val index4 = stat.lastIndexOf('}')
                    score.add(stat.substring(index1+1,index2).toInt())
                    status.add(stat.substring(index3+1,index4).toInt())
                }
                else{
                    it.exception
                    score[0]=0
                    status[0]=0
                    Toast.makeText(this,it.toString(),Toast.LENGTH_LONG).show()
                }
            }*/
        }
        else{
            Toast.makeText(this,"no current user",Toast.LENGTH_LONG).show()
        }

        levelList.add(Level("title", "bob", score[0], status[0], 1))
        levelList.add(Level("title", "bob", 0, 0, 2))
        levelList.add(Level("title", "bob", 0, -1, 3))
        levelList.add(Level("title", "bob", 0, -1, 4))
        levelList.add(Level("title", "bob", 0, -1, 5))

        rLevelView.adapter.notifyDataSetChanged()

    }
}
