package edu.uco.rsteele5.gravityrunner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

const val LEVEL = "level"
const val CURRENTCOSTUME = "currentCostume"

class LevelArrayAdapter(val context: Context, var levelList: ArrayList<Level>) :
    RecyclerView.Adapter<LevelArrayAdapter.ViewHolder>() {

    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()!!
    private val current = mAuth.currentUser?.email

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
            val colorIdDisabled = context.resources.getColor(R.color.disable)
            val colorIdEnabled = context.resources.getColor(R.color.basic)
            val item = itemView.findViewById<ConstraintLayout>(R.id.cView)
            val imgView = itemView.findViewById<ImageView>(R.id.imgCostume)
            val titleView = itemView.findViewById<TextView>(R.id.tCostumeName)
            val scoreView = itemView.findViewById<TextView>(R.id.tHighScore)
            val statusView = itemView.findViewById<Button>(R.id.btnCostumeStatus)

            imgView.setImageResource(resourceId)
            titleView.text = levelList[position].title

            if (levelList[position].status != -1) {
                if(levelList[position].status > 0)
                    statusView.text = context.getString(R.string.leaderBoard)
                else
                    statusView.text = context.getString(R.string.uncleared)
                item.setBackgroundColor(colorIdEnabled)
                scoreView.text = levelList[position].score.toString()
                itemView.isClickable = true
                itemView.setOnClickListener {
                    db.collection("$current").document("Costumes").get().addOnSuccessListener { it ->
                        val currentCostume = it.getString("Equipped")
                        Log.d("costume", "$currentCostume")
                        val curCostumeNum =
                        when (currentCostume) {
                            "Dragon" -> 0
                            "Knight" -> 1
                            "Wizard" -> 2
                            else -> -1
                        }
                        context.myStartActivityForResult<GameEngine>(1, levelList[position].level, curCostumeNum)
                    }
                }
                statusView.setOnClickListener {
                    //open leader board activity
                    val i = Intent(context, LeaderBoard::class.java)
                    i.putExtra(LEVEL,levelList[position].level)
                    context.startActivity(i)
                }
            }else {
                statusView.text = context.getString(R.string.locked)
                item.setBackgroundColor(colorIdDisabled)
                statusView.setOnClickListener {}
                itemView.setOnClickListener{}
                itemView.isClickable = false
            }
        }
    }

    inline fun <reified T: Activity> Context.myStartActivityForResult(requestCode: Int, level: Int, costume: Int) {
        val intent = Intent(this, T::class.java)
        intent.putExtra(LEVEL, level)
        if(costume != -1)
            intent.putExtra(COSTUME, costume)
        startActivityForResult(context as Activity, intent, requestCode, null)
    }
}