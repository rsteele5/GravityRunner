package edu.uco.rsteele5.gravityrunner

import android.app.Activity
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.store_item.view.*

//TODO: FILL ADAPTER FIELD
const val EQUIP = "equipment"

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
            val resourceId = context.resources.getIdentifier(storeList[position].src, "drawable", context.packageName)
            val colorId = context.resources.getColor(R.color.disable)
            val item = itemView.findViewById<ConstraintLayout>(R.id.cCostumeView)
            val imgView = itemView.findViewById<ImageView>(R.id.imgCostume)
            val titleView = itemView.findViewById<TextView>(R.id.tCostumeName)
            val statusView = itemView.findViewById<Button>(R.id.btnCostumeStatus)
            val db = FirebaseFirestore.getInstance()
            val mAuth = FirebaseAuth.getInstance()
            val current = mAuth.currentUser?.email
            val cosRef = db?.collection("$current")?.document("Costumes")

            imgView.setImageResource(resourceId)
            titleView.text = storeList[position].title

            when (storeList[position].status) {
                -1 -> {
                    statusView.text = "locked"
                    item.setBackgroundColor(colorId)
                    //todo: can buy this item
                }
                0 -> {
                    statusView.text = "unlocked"
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
                            }
                        }
                    }
                }
                1 -> {
                    statusView.text = "equipped"
                }
            }
        }
    }
}