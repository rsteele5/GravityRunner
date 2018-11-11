package edu.uco.rsteele5.gravityrunner.Control

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import edu.uco.rsteele5.gravityrunner.OrientationManager
import edu.uco.rsteele5.gravityrunner.Renderable
import edu.uco.rsteele5.gravityrunner.model.Level
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector
import java.util.concurrent.CopyOnWriteArrayList

class LevelController(r: Resources, val screenWidth: Float, val screenHeight: Float) : Renderable {

    val levels = CopyOnWriteArrayList<Level>()
    var currentLevel: Level? = null
    var resources: Resources? = null

    init {
        this.resources = r
    }
    override fun update(orientation: OrientationManager.ScreenOrientation, motionVector: PhysicsVector) {
        currentLevel!!.update(orientation,motionVector)
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        currentLevel!!.draw(canvas,paint)
    }

    fun loadLevelOne(){
        val map = CopyOnWriteArrayList<CopyOnWriteArrayList<Int>>()
        val rows = 14
        val cols = 12

        for(i in 0..(rows-1)){
            map.add(CopyOnWriteArrayList())
        }

        /* TODO: Goal
        2,2,2,2,2,2,2,2,2,2,2,2
        2,0,0,0,0,0,0,0,0,0,0,2
        2,0,0,0,0,0,0,0,0,0,0,2
        2,0,0,0,0,0,0,0,0,0,0,2
        2,1,0,0,0,0,0,0,0,0,0,2
        2,2,2,2,2,2,2,2,0,0,0,2
        2,0,0,0,0,0,0,2,0,0,0,2
        2,0,0,0,0,0,0,2,0,0,0,2
        2,0,0,0,0,0,0,2,0,0,0,2
        2,2,2,2,2,2,2,2,0,0,0,2
        2,2,0,0,0,0,0,6,0,0,0,2
        2,2,0,2,2,2,2,2,2,2,2,2
        2,2,0,0,0,0,0,0,0,0,3,2
        2,2,2,2,2,2,2,2,2,2,2,2
         */
        val mapArray: Array<Array<Int>> = 
            arrayOf(
                arrayOf(2,2,2,2,2,2,2,2,2,2,2,2),
                arrayOf(2,0,0,0,0,0,0,0,0,0,0,2),
                arrayOf(2,0,0,0,0,0,0,0,0,0,0,2),
                arrayOf(2,0,0,0,0,0,0,0,0,0,0,2),
                arrayOf(2,1,0,0,0,0,0,0,0,0,0,2),
                arrayOf(2,2,2,2,2,2,2,2,0,0,0,2),
                arrayOf(2,0,0,0,0,0,0,2,0,0,0,2),
                arrayOf(2,0,0,0,0,0,0,2,0,0,0,2),
                arrayOf(2,0,0,0,0,0,0,2,0,0,0,2),
                arrayOf(2,2,2,2,2,2,2,2,0,0,0,2),
                arrayOf(2,2,0,0,0,0,0,6,0,0,0,2),
                arrayOf(2,2,0,2,2,2,2,2,2,2,2,2),
                arrayOf(2,2,0,0,0,0,0,0,0,0,3,2), //TODO: Change to have goal.
                arrayOf(2,2,2,2,2,2,2,2,2,2,2,2))

        for(i in 0..(rows-1)){
            map[i].addAll(mapArray[i])
        }

        currentLevel = Level(resources!!, map, screenWidth, screenHeight)
        currentLevel!!.createLevel()
    }


}