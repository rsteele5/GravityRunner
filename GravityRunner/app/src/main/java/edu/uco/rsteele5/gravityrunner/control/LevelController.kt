package edu.uco.rsteele5.gravityrunner.control

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import edu.uco.rsteele5.gravityrunner.Renderable
import edu.uco.rsteele5.gravityrunner.model.Level
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector
import java.util.concurrent.CopyOnWriteArrayList

class LevelController(r: Resources, private val screenWidth: Float, private val screenHeight: Float) : Renderable {

    var currentLevel: Level? = null
    var resources: Resources? = null

    init {
        this.resources = r
    }

    fun loadLevel(num: Int){
        val map = CopyOnWriteArrayList<CopyOnWriteArrayList<Int>>()
        val mapArray: Array<Array<Int>> =
        when(num){
            1 -> {loadLevelOne()}
            2 -> {loadLevelTwo()}
            3 -> {loadLevelThree()}
            4 -> {loadLevelFour()}
            5 -> {loadLevelFive()}
            else -> {arrayOf(
                arrayOf(2,2,2),
                arrayOf(2,1,2),
                arrayOf(2,2,2))}
        }

        for(i in 0..(mapArray.size-1)){
            map.add(CopyOnWriteArrayList())
            map[i].addAll(mapArray[i])
        }

        currentLevel = Level(resources!!, map, screenWidth, screenHeight)
        currentLevel!!.createLevel()
    }

    fun isCurrentLevelLoaded(): Boolean{
        return currentLevel!!.checkLoadStatus()
    }

    override fun update(orientation: OrientationManager.ScreenOrientation, motionVector: PhysicsVector) {
        currentLevel!!.update(orientation,motionVector)
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        currentLevel!!.draw(canvas,paint)
    }

    fun loadLevelOne(){
        val mapArray: Array<Array<Int>> = 
            arrayOf(
                arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2),
                arrayOf(2, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 2),
                arrayOf(2, 0, 0, 0, 0,10 ,0 ,0, 0, 0, 0, 2),
                arrayOf(2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2),
                arrayOf(2, 1, 0, 0, 0, 4, 0, 0, 0, 0,11, 2),
                arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 2),
                arrayOf(2, 0, 0, 0, 0, 0, 0, 2, 0,10 ,7 ,2),
                arrayOf(2, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2),
                arrayOf(2, 0, 0, 0, 0, 0, 0, 2, 0, 0, 9, 2),
                arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 0,11, 0, 2),
                arrayOf(2, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2),
                arrayOf(2, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 2),
                arrayOf(2, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2),
                arrayOf(2, 0, 0, 0, 0, 0, 0,10, 0, 0, 0, 2),
                arrayOf(2, 0, 0, 0, 0, 4, 0, 0, 0, 0, 3, 2), //TODO: Change to have goal.
                arrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2))

    }

    private fun loadLevelTwo(): Array<Array<Int>> {
        return arrayOf(
            arrayOf(2,2,2,2),
            arrayOf(2,0,0,2),
            arrayOf(2,1,0,2),
            arrayOf(2,2,2,2))
    }

    private fun loadLevelThree(): Array<Array<Int>> {
        return arrayOf(
            arrayOf(2,2,2,2,2),
            arrayOf(2,0,0,0,2),
            arrayOf(2,0,0,0,2),
            arrayOf(2,1,0,0,2),
            arrayOf(2,2,2,2,2))
    }

    private fun loadLevelFour(): Array<Array<Int>> {
        return arrayOf(
            arrayOf(2,2,2,2,2,2),
            arrayOf(2,0,0,0,0,2),
            arrayOf(2,0,0,0,0,2),
            arrayOf(2,0,0,0,0,2),
            arrayOf(2,0,0,0,0,2),
            arrayOf(2,1,0,0,0,2),
            arrayOf(2,2,2,2,2,2))
    }

    private fun loadLevelFive(): Array<Array<Int>> {
        return arrayOf(
            arrayOf(2,2,2,2,2,2,2),
            arrayOf(2,0,0,0,0,0,2),
            arrayOf(2,0,0,0,0,0,2),
            arrayOf(2,0,0,0,0,0,2),
            arrayOf(2,0,0,0,0,0,2),
            arrayOf(2,0,0,0,0,0,2),
            arrayOf(2,1,0,0,0,0,2),
            arrayOf(2,2,2,2,2,2,2))
    }

}