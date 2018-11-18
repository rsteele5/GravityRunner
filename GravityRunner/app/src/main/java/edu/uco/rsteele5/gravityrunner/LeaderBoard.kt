package edu.uco.rsteele5.gravityrunner

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_leader_board.*

class LeaderBoard : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var db: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_board)
        val i = intent.getIntExtra("level", -1)
        setTitle(getString(R.string.leaderTitle, i))

        val list = ArrayList<String>()
        var adt: ArrayAdapter<String>? = null

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        /*if(db!=null) {
            db?.collection("LeaderBoard/Levels/Level1")?.get()
                ?.addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            list.add(document.data)
                        }else{
                            list.add(task.exception)
                        }
                    }
                })
        }*/ //read data

        adt = ArrayAdapter (this,android.R.layout.simple_list_item_1,list)
        lLeader.adapter = adt
    }
}
