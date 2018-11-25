package edu.uco.rsteele5.gravityrunner

import android.content.Intent
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
        var equipped: String?

        var status = ArrayList<Int?>()
        var title = ArrayList<String?>()

        var cosRef = db?.collection("$current")?.document("Costumes")


        val thread = object : Thread() {

            override fun run() {
                try {
                    while (!this.isInterrupted) {
                        Thread.sleep(1000)
                        runOnUiThread {
                            // update TextView here!
                            db.collection("CostumeTitle").document("Properties").get().addOnSuccessListener {
                                title.clear()
                                title.add(it.getString("Dragon"))
                                title.add(it.getString("Knight"))
                                title.add(it.getString("Wizard"))
                            }
                            cosRef?.get()
                                ?.addOnSuccessListener {
                                    status.clear()
                                    status.add(it?.getDouble("Dragon")?.toInt())
                                    status.add(it?.getDouble("Knight")?.toInt())
                                    status.add(it?.getDouble("Wizard")?.toInt())
                                    equipped = it.getString("Equipped")

                                    var currentCostume: String = "nothing"

                                    //todo make costume field in db
                                    storeList.clear()
                                    for (i in 0..2) {

                                        storeList.add(Store("bob", title[i], status[i]))
                                        lStore.adapter.notifyDataSetChanged()
                                    }
                                    lStore.adapter.notifyDataSetChanged()
                                }
                        }
                    }
                } catch (e: InterruptedException) {
                }
            }
        }
        thread.start()
    }
}
