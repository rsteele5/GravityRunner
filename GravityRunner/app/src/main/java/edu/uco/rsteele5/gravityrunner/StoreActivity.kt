package edu.uco.rsteele5.gravityrunner

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_store.*

class StoreActivity : AppCompatActivity() {

    private var storeList = ArrayList<Store>(3)
    private var spinner: ProgressBar? = null
    private var dragonHat: Store? = null
    private var knightHat: Store? = null
    private var wizardHat: Store? = null
    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private val current = mAuth.currentUser?.email
    private val storage = FirebaseStorage.getInstance()
    private val storeRef1 = storage.reference.child("Costumes/dragon.png")
    private val storeRef2 = storage.reference.child("Costumes/knight.png")
    private val storeRef3 = storage.reference.child("Costumes/wizard.png")
    private val costumeReference = db.collection("$current").document("Costumes")
    private val coinsReference = db.collection("$current").document("Coins")
    private var coins: Int? = 0
    private var addedItems = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        lStore.layoutManager = LinearLayoutManager(this)
        lStore.adapter = StoreArrayAdapter(this, storeList)

        spinner = findViewById(R.id.progressBar1)


        //Start the DB calls and show the spinner
        spinner!!.visibility = View.VISIBLE

        //Add coins to the view
        db.collection("$current").document("Coins").get().addOnSuccessListener {
            coins = it.getDouble("amount")?.toInt()
            id_coinAmount.text = coins.toString()
            checkLoaded()
        }

        //Add the Dragon to the list
        storeRef1.downloadUrl.addOnSuccessListener { imgResult ->
            var imageString = imgResult.toString()
            db.collection("CostumeTitle")
                .document("Properties").get()
                .addOnSuccessListener { titleResult ->
                var itemTitle = titleResult.getString("Dragon")

                costumeReference.get()
                    .addOnSuccessListener { statusResult ->
                        var itemStatus = statusResult.getDouble("Dragon")?.toInt()
                        dragonHat = Store(imageString, itemTitle, itemStatus)
                        checkLoaded()
                    }
            }
        }

        //Add the Knight to the list
        storeRef2.downloadUrl.addOnSuccessListener { imgResult ->
            var imageString = imgResult.toString()
            db.collection("CostumeTitle")
                .document("Properties").get()
                .addOnSuccessListener { titleResult ->
                    var itemTitle = titleResult.getString("Knight")

                    costumeReference.get()
                        .addOnSuccessListener { statusResult ->
                            var itemStatus = statusResult.getDouble("Knight")?.toInt()
                            knightHat = Store(imageString, itemTitle, itemStatus)
                            checkLoaded()
                        }
                }
        }

        //Add the Wizard to the list
        storeRef3.downloadUrl.addOnSuccessListener { imgResult ->
            var imageString = imgResult.toString()
            db.collection("CostumeTitle")
                .document("Properties").get()
                .addOnSuccessListener { titleResult ->
                    var itemTitle = titleResult.getString("Wizard")

                    costumeReference.get()
                        .addOnSuccessListener { statusResult ->
                            var itemStatus = statusResult.getDouble("Wizard")?.toInt()
                            wizardHat = Store(imageString, itemTitle, itemStatus)
                            checkLoaded()
                        }
                }
        }
    }

    private fun checkLoaded(){
        addedItems++
        if(addedItems >= 4){
            spinner!!.visibility = View.GONE
            storeList.add(dragonHat!!)
            storeList.add(knightHat!!)
            storeList.add(wizardHat!!)
            lStore.adapter.notifyDataSetChanged()
        }
    }

    override fun onPause() {
        super.onPause()
        for(i in 0 until storeList.size) {
            costumeReference.update("${storeList[i].title}", storeList[i].status)
        }
        coinsReference.update("amount", id_coinAmount.text.toString().toInt())
    }

    fun subtractCoins(cost: Int): Boolean {
        //var currentCoins = id_coinAmount.text.toString().toInt()
        return if(coins!! >= cost){
            coins = coins!! - cost
            id_coinAmount.text = coins.toString()
            true
        } else {
            false
        }
    }

    fun updateDataSet() {
        lStore.adapter.notifyDataSetChanged()
    }
}
