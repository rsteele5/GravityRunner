package edu.uco.rsteele5.gravityrunner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

const val LEVEL = "level"

class LevelArrayAdapter(val context: Context, var levelList: ArrayList<Level>) :
    RecyclerView.Adapter<LevelArrayAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.level_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return levelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(position)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bindItem(position: Int) {
            val resourceId = context.resources.getIdentifier(levelList[position].src, "drawable", context.packageName)
            val colorId = context.resources.getColor(R.color.disable)
            val item = itemView.findViewById<ConstraintLayout>(R.id.cView)
            val imgView = itemView.findViewById<ImageView>(R.id.imgLevel)
            val titleView = itemView.findViewById<TextView>(R.id.tStageName)
            val scoreView = itemView.findViewById<TextView>(R.id.tHighScore)
            val statusView = itemView.findViewById<Button>(R.id.btnStatus)

            imgView.setImageResource(resourceId)
            titleView.text = levelList[position].title
            if (levelList[position].score == null)
                scoreView.text = "-"
            else
                scoreView.text = levelList[position].score.toString()
            if (levelList[position].status != -1) {
                statusView.text = context.getString(R.string.leaderBoard)
                itemView.setOnClickListener {
                    //open each level activity
                    var lev = Level("","",0,0,levelList[position].level).engine().toString()
                    var type:Class<*> = Class.forName("edu.uco.rsteele5.gravityrunner."+lev)
                    val i = Intent(context, type)
                    context.startActivity(i)
                }
                statusView.setOnClickListener {
                    //open leader board activity

                    val i = Intent(context, LeaderBoard::class.java)
                    i.putExtra(LEVEL,levelList[position].level)
                    context.startActivity(i)
                }
            }else {
                statusView.text = context.getString(R.string.locked)
                item.setBackgroundColor(colorId)
                itemView.isClickable = false
            }
        }
    }
}