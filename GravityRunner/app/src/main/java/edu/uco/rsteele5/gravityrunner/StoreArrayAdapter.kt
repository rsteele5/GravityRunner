package edu.uco.rsteele5.gravityrunner

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

//TODO: FILL ADAPTER FIELD
const val EQUIP ="equipment"

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
            var equiped:String? = "basic"
            var i = Intent(context,StoreActivity::class.java)

            imgView.setImageResource(resourceId)
            titleView.text = storeList[position].title

            when (storeList[position].status) {
                -1 -> {
                    statusView.text = "locked"
                    item.setBackgroundColor(colorId)
                }
                0 -> {
                    statusView.text = "unlocked"
                    item.setOnClickListener {
                        equiped = storeList[position].title
                    }
                }
                1 -> {
                    statusView.text = "equipped"
                }
            }
            i.putExtra(EQUIP,equiped)
        }
    }
}