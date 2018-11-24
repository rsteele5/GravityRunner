package edu.uco.rsteele5.gravityrunner

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_store.*

class StoreActivity : AppCompatActivity() {

    private var storeList = ArrayList<Store>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        lStore.layoutManager = LinearLayoutManager(this)
        lStore.adapter = StoreArrayAdapter(this, storeList)

        var db = FirebaseFirestore.getInstance()
        var mAuth = FirebaseAuth.getInstance()
        var current = mAuth.currentUser?.email

        var status = ArrayList<Int?>()
        var title = ArrayList<String?>()

        var cosRef = db?.collection("$current")?.document("Costumes")

        db.collection("CostumeTitle").document("Properties").get().addOnSuccessListener {
            title.add(it.getString("Dragon"))
            title.add(it.getString("Knight"))
            title.add(it.getString("Wizard"))
        }
        cosRef?.get()
            ?.addOnSuccessListener {
                status.add(it?.getDouble("Dragon")?.toInt())
                status.add(it?.getDouble("Knight")?.toInt())
                status.add(it?.getDouble("Wizard")?.toInt())

                var stat = intent.getStringExtra(EQUIP)
                var currentCostume: String

                //todo make costume field in db
                //todo status from db

                for (i in 0..2) {
                    storeList.add(Store("bob", title[i], status[i]))
                    lStore.adapter.notifyDataSetChanged()
                    if (status[i] == 1) {
                        currentCostume =
                                when (i) {
                                    0 -> "Dragon"
                                    1 -> "Knight"
                                    else -> "Wizard"
                                }
                    }
                }/*
                lStore.setOnClickListener {
                    if(){
                        cosRef?.update("Wizard",1)
                        cosRef?.update("$currentCostume",0)

                    }
                }*/
                lStore.adapter.notifyDataSetChanged()
            }


    }
}
