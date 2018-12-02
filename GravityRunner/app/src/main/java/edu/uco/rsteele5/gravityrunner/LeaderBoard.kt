package edu.uco.rsteele5.gravityrunner

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_leader_board.*

class LeaderBoard : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var db: FirebaseFirestore? = null

    var LeaderList = ArrayList<Leader>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_board)
        val i = intent.getIntExtra(LEVEL, 0)
        tRank.text = getString(R.string.leaderTitle,i)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val listViewAdapter = ArrayAdapter<Leader>(this, android.R.layout.simple_list_item_1, LeaderList)
        lLeader.adapter = listViewAdapter

        db?.collection("LeaderBoard/Levels/Level$i")
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
            }
        db?.collection("LeaderBoard/Levels/Level$i")?.get()?.addOnSuccessListener {
            var num = it.size() - 1
            var numList = ArrayList<Int>()
            for(i in 0..num)
            {
                numList.add(i+1)
            }
            var adapter2 = ArrayAdapter<Int>(this, android.R.layout.simple_list_item_1, numList)
            lNumber.adapter = adapter2
            adapter2.notifyDataSetChanged()
        }

    }
}
