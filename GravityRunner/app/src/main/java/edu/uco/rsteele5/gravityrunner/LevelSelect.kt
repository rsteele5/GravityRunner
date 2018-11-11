package edu.uco.rsteele5.gravityrunner

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_level_select.*

class LevelSelect : AppCompatActivity() {

    private var levelList = ArrayList<Level>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)
        title = getString(R.string.level_activity)

        rLevelView.layoutManager = LinearLayoutManager(this)
        rLevelView.adapter = LevelArrayAdapter(this, levelList)

        levelList.add(Level("title","bob",30,1))
        levelList.add(Level("title","bob",null,0))
        levelList.add(Level("title","bob",null,-1))

        rLevelView.adapter.notifyDataSetChanged()

    }
}
