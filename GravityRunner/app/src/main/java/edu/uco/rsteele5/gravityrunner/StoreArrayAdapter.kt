package edu.uco.rsteele5.gravityrunner

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StoreArrayAdapter(val context: Context, var storeList: ArrayList<Store>) :
    RecyclerView.Adapter<StoreArrayAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.store_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return storeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(position)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bindItem(position: Int) {
            val colorId = context.resources.getColor(R.color.disable)
            val colorId2 = context.resources.getColor(R.color.basic)
            val item = itemView.findViewById<ConstraintLayout>(R.id.cCostumeView)
            val imgView = itemView.findViewById<ImageView>(R.id.imgCostume)
            val titleView = itemView.findViewById<TextView>(R.id.tCostumeName)
            val statusView = itemView.findViewById<Button>(R.id.btnCostumeStatus)
            val db = FirebaseFirestore.getInstance()
            val mAuth = FirebaseAuth.getInstance()
            val current = mAuth.currentUser?.email
            val cosRef = db?.collection("$current")?.document("Costumes")
            val coinRef = db.collection("$current").document("Coins")
            val price:Int = 200

            Glide.with(context).load("${storeList[position].src}").into(imgView)
            item.removeView(imgView)
            item.addView(imgView)

            titleView.text = storeList[position].title

            when (storeList[position].status) {
                -1 -> {
                    statusView.text = "locked"
                    item.setBackgroundColor(colorId)

                    statusView.setOnClickListener {
                        var currentCoin: Int? = 0
                        coinRef.get().addOnSuccessListener {
                            currentCoin = it.getDouble("amount")?.toInt()
                            currentCoin?.minus(price)
                            cosRef.update("${storeList[position].title}", 0)
                            coinRef.update("amount", currentCoin)
                            storeList[position].status = 0
                        }
                    }

                }
                0 -> {
                    statusView.text = "unlocked"
                    item.setBackgroundColor(colorId2)
                    statusView.setOnClickListener {
                        cosRef.update("Equipped", storeList[position].title)
                        statusView.text = "equipped"
                        var currentCostume = "nothing"
                        for (i in 0..2) {
                            if (storeList[i].status == 1) {
                                currentCostume =
                                        when (i) {
                                            0 -> "Dragon"
                                            1 -> "Knight"
                                            else -> "Wizard"
                                        }
                                if (storeList[position].title != "nothing" && storeList[position].title != currentCostume) {
                                    cosRef.update("${storeList[position].title}", 1)
                                    cosRef.update("$currentCostume", 0)
                                    storeList[i].status = 0
                                }
                            } else {
                                cosRef.update("${storeList[position].title}", 1)
                                storeList[position].status = 1
                            }
                        }
                    }
                }
                1 -> {
                    statusView.text = "equipped"
                    statusView.setOnClickListener {
                        cosRef.update("Equipped", "nothing")
                        statusView.text = "unlocked"
                        cosRef.update("${storeList[position].title}", 0)
                        storeList[position].status = 0
                    }
                }
            }
        }
    }
}