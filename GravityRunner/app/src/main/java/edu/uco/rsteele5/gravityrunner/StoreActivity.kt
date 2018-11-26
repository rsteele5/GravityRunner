package edu.uco.rsteele5.gravityrunner

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_store.*

class StoreActivity : AppCompatActivity() {

    private var storeList = ArrayList<Store>()
    private var imgList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        lStore.layoutManager = LinearLayoutManager(this)
        lStore.adapter = StoreArrayAdapter(this, storeList)

        var db = FirebaseFirestore.getInstance()
        var mAuth = FirebaseAuth.getInstance()
        var current = mAuth.currentUser?.email
        var storage = FirebaseStorage.getInstance()
        var storeRef1 = storage.reference.child("Costumes/dragon.png")
        var storeRef2 = storage.reference.child("Costumes/knight.png")
        var storeRef3 = storage.reference.child("Costumes/wizard.png")
        var equipped: String?

        var status = ArrayList<Int?>()
        var title = ArrayList<String?>()

        var cosRef = db?.collection("$current")?.document("Costumes")
        

        storeRef1.downloadUrl.addOnSuccessListener {
            imgList.add(it.toString())
            storeRef2.downloadUrl.addOnSuccessListener {
                imgList.add(it.toString())
                storeRef3.downloadUrl.addOnSuccessListener {
                    imgList.add(it.toString())

                    try {
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

                                    storeList.clear()
                                    for (i in 0..2) {
                                        Log.d("s", "${imgList.size}")
                                        Log.d("s", "${title.size}")
                                        Log.d("s", "${status.size}")
                                        storeList.add(Store(imgList[i], title[i], status[i]))
                                        lStore.adapter.notifyDataSetChanged()
                                    }
                                    lStore.adapter.notifyDataSetChanged()
                                }
                    } catch (e: InterruptedException) {
                    }
                }
            }
        }
    }

    override fun onBackPressed() {

        finish()
    }
}
