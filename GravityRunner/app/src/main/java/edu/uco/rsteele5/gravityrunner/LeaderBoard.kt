package edu.uco.rsteele5.gravityrunner

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_leader_board.*

class LeaderBoard : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var db: FirebaseFirestore? = null

    var LeaderList = ArrayList<Leader>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_board)
        val i = intent.getIntExtra("level", -1)
        tRank.text = getString(R.string.leaderTitle,i)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val listViewAdapter = ArrayAdapter<Leader>(this, android.R.layout.simple_list_item_1, LeaderList)
        lLeader.adapter = listViewAdapter

            db?.collection("LeaderBoard/Levels/Level1")
                ?.orderBy("score", Query.Direction.DESCENDING)
                ?.get()
                ?.addOnSuccessListener{
                    LeaderList.clear()
                    for(docSnapshot in it){
                        val leader = docSnapshot.toObject(Leader::class.java)
                        leader.id = docSnapshot.id
                        LeaderList.add(leader)
                    }
                    val adapter = lLeader.adapter as ArrayAdapter<Leader>
                    adapter.notifyDataSetChanged()
                    //
                    //
                    //
        }
    }
}
