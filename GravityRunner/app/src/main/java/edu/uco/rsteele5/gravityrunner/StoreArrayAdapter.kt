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
import android.widget.Toast
import com.bumptech.glide.Glide

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
            val colorId = context.resources.getColor(R.color.disable, context.theme)
            val colorId2 = context.resources.getColor(R.color.basic, context.theme)
            val colorId3 = context.resources.getColor(R.color.equipped, context.theme)
            val item = itemView.findViewById<ConstraintLayout>(R.id.cCostumeView)
            val imgView = itemView.findViewById<ImageView>(R.id.imgCostume)
            val titleView = itemView.findViewById<TextView>(R.id.tCostumeName)
            val statusView = itemView.findViewById<Button>(R.id.btnCostumeStatus)

            Glide.with(context).load(storeList[position].src).into(imgView)

            titleView.text = storeList[position].title

            when (storeList[position].status) {
                -1 -> {
                    statusView.text = "Buy"
                    item.setBackgroundColor(colorId)
                    statusView.setOnClickListener {
                        if((context as StoreActivity).subtractCoins(10)){
                            storeList[position].status = 0
                            context.updateDataSet()
                        } else {
                            Toast.makeText(context, "You don't have enough money", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                0 -> {
                    statusView.text = "Equip"
                    item.setBackgroundColor(colorId2)
                    statusView.setOnClickListener {
                        for (i in 0..2) {
                            if (storeList[i].status == 1) {
                                storeList[i].status = 0
                            }
                        }
                        storeList[position].status = 1
                        (context as StoreActivity).updateDataSet()
                    }
                }
                1 -> {
                    statusView.text = "Unequip"
                    item.setBackgroundColor(colorId3)
                    statusView.setOnClickListener {
                        storeList[position].status = 0
                        (context as StoreActivity).updateDataSet()
                    }
                }
            }
        }
    }
}