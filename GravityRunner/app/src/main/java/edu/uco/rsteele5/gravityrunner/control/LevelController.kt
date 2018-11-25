package edu.uco.rsteele5.gravityrunner.control

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import edu.uco.rsteele5.gravityrunner.R
import edu.uco.rsteele5.gravityrunner.model.Renderable
import edu.uco.rsteele5.gravityrunner.model.Level
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector
import java.util.concurrent.CopyOnWriteArrayList

class LevelController(r: Resources,private val screenWidth: Float,private val screenHeight: Float) :
    Renderable {

    var currentLevel: Level? = null
    var resources: Resources? = null
    var currentPoints: Long = 0

    init {
        this.resources = r
    }

    fun loadLevel(num: Int): Long{
        var pointMax: Long
        val map = CopyOnWriteArrayList<CopyOnWriteArrayList<Int>>()
        val mapArray: Array<Array<Int>> =
        when(num){
            1 -> {pointMax = 120000; loadLevelOne()}
            2 -> {pointMax = 120000; loadLevelTwo()}
            3 -> {pointMax = 120000; loadLevelThree()}
            4 -> {pointMax = 120000; loadLevelFour()}
            5 -> {pointMax = 120000; loadLevelFive()}
            else -> {pointMax = 120000
                arrayOf(
                arrayOf(2,2,2),
                arrayOf(2,1,2),
                arrayOf(2,2,2))}
        }
        val background: Bitmap =
            when(num){
                1 -> BitmapFactory.decodeResource(resources,R.drawable.background1)
                2 -> BitmapFactory.decodeResource(resources,R.drawable.background2)
                3 -> BitmapFactory.decodeResource(resources,R.drawable.background3)
                4 -> BitmapFactory.decodeResource(resources,R.drawable.background4)
                5 -> BitmapFactory.decodeResource(resources,R.drawable.background5)
                else -> BitmapFactory.decodeResource(resources,R.drawable.background1)
            }

        for(i in 0..(mapArray.size-1)){
            map.add(CopyOnWriteArrayList())
            map[i].addAll(mapArray[i])
        }

        currentLevel = Level(resources!!, map, screenWidth, screenHeight, pointMax)
        currentLevel!!.createLevel()
        return pointMax
    }

    fun isCurrentLevelLoaded(): Boolean{
        return currentLevel!!.checkLoadStatus()
    }

    override fun update(orientation: OrientationManager.ScreenOrientation,motionVector: PhysicsVector) {
        currentLevel!!.update(orientation,motionVector)
    }

    override fun draw(canvas: Canvas,paint: Paint) {
        currentLevel!!.draw(canvas,paint)
    }

    //SPAWN = 1
    //WALL = 2
    //GOAL = 3
    //SPIKES = 4
    //SPIKES_RIGHT = 5
    //SPIKES_DOWN = 6
    //SPIKES_LEFT = 7
    //BAT = 8
    //SPEEDBOOST = 9
    //COIN = 10
    //ARMOR = 11
    //JUMPBOOST = 12
    fun loadLevelOne(): Array<Array<Int>> {
        return arrayOf(
            arrayOf(2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2),
            arrayOf(2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,6,6,6,6,6,6,6,6,2,2,2,2,2,2,2),
            arrayOf(2,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,13,0,13,0,13,0,13,0,0,0,0,0,0,2),
            arrayOf(2,5,0,0,0,0,0,0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,13,0,0,0,0,2),
            arrayOf(2,5,0,0,0,0,0,0,0,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,2),
            arrayOf(2,5,0,0,0,13,2,2,2,2,2,2,2,2,2,4,4,4,4,4,4,4,4,2,2,2,2,2,2,2),
            arrayOf(2,5,0,0,0,13,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2),
            arrayOf(2,5,0,0,0,13,2,2,2,2,2,2,2,2,2,2,6,6,2,2,2,2,2,2,2,2,2,2,2,2),
            arrayOf(2,5,0,0,0,0,11,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,6,6,0,0,0,0,0,2),
            arrayOf(2,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,0,0,0,0,0,0,0,0,2),
            arrayOf(2,5,0,0,0,0,0,0,0,0,0,0,0,4,4,4,0,0,0,0,0,0,13,0,0,0,0,0,0,2),
            arrayOf(2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,2),
            arrayOf(2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,13,0,0,0,13,0,0,0,0,0,0,2),
            arrayOf(2,0,0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2),
            arrayOf(2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2),
            arrayOf(2,2,2,2,2,2,2,2,4,4,4,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2),
            arrayOf(2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2)
        )
    }

    private fun loadLevelTwo(): Array<Array<Int>> {
        return arrayOf(
            arrayOf(2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2),
            arrayOf(2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,13,0,13,0,13,0,13,0,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,0,9,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,2),
            arrayOf(2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,8,0,0,2,2,2,2,0,2,2,4,4,2,0,2,13,13,2,0,0,0,0,0,0,0,0,10,13,0,0,0,0,0,0,0,0,0,7,2,8,0,0,0,2),
            arrayOf(2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,0,2,2,2,2,2,0,2,13,13,2,0,0,0,0,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,2),
            arrayOf(2,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,13,0,0,0,0,0,0,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,0,2,2,2,2,2,0,2,13,13,2,0,0,0,0,0,0,10,0,13,0,0,0,0,0,13,13,13,13,13,0,0,0,0,0,0,2),
            arrayOf(2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,0,2,2,2,2,2,0,2,13,13,2,0,0,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,2),
            arrayOf(2,0,0,0,0,0,13,13,0,0,0,0,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,0,13,0,0,0,0,0,0,0,0,0,0,2,2,2,2,0,2,2,2,2,2,0,2,13,13,2,0,0,0,0,10,0,13,0,0,0,0,0,0,0,0,0,0,0,0,7,2,0,0,0,0,2),
            arrayOf(2,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,0,2,2,2,2,2,0,2,2,2,2,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,2),
            arrayOf(2,1,0,0,0,0,4,4,0,0,2,2,2,2,2,2,2,2,2,2,4,4,4,4,4,4,4,4,4,4,4,4,4,4,2,2,0,0,0,0,2,2,2,2,2,2,2,4,4,4,0,0,0,11,0,0,9,0,0,0,0,0,0,0,2,2,2,2,2,0,0,0,0,0,0,0,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,2,9,0,0,3,2),
            arrayOf(2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2)
        )
    }
    
    private fun loadLevelThree(): Array<Array<Int>> {
        return arrayOf(
            arrayOf(2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2),
            arrayOf(2,0,1,0,2,0,3,0,0,6,6,6,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,2),
            arrayOf(2,0,0,0,2,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,2),
            arrayOf(2,0,0,0,2,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,2),
            arrayOf(2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2),
            arrayOf(2,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,2),
            arrayOf(2,0,0,0,2,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,2,2,0,0,0,2),
            arrayOf(2,0,11,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,2,0,0,0,2),
            arrayOf(2,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,4,0,0,0,0,0,7,2,0,0,0,2),
            arrayOf(2,2,0,0,0,4,0,0,0,0,0,0,9,0,0,13,0,7,2,5,0,0,0,0,7,2,0,0,0,2),
            arrayOf(2,5,0,0,7,2,5,0,0,0,0,0,0,0,0,0,0,0,6,0,0,0,0,0,7,2,0,0,0,2),
            arrayOf(2,5,0,0,0,6,0,0,13,0,0,0,0,0,0,0,0,0,0,0,0,4,0,0,7,2,0,0,2,2),
            arrayOf(2,5,0,0,0,0,0,0,0,0,0,0,8,0,0,13,0,0,0,0,7,2,5,0,7,2,0,8,2,2),
            arrayOf(2,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,0,0,6,0,0,7,2,0,0,2,2),
            arrayOf(2,5,0,0,0,4,0,0,13,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,2,0,13,2,2),
            arrayOf(2,5,0,0,7,2,5,0,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,7,2,0,0,2,2),
            arrayOf(2,5,0,0,0,6,0,0,0,0,0,7,2,5,0,13,0,0,9,0,0,0,0,0,7,2,0,0,2,2),
            arrayOf(2,5,0,0,0,0,0,0,9,0,0,0,6,0,0,0,0,0,0,0,0,0,11,0,7,2,0,13,2,2),
            arrayOf(2,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,2,0,0,2,2),
            arrayOf(2,2,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,0,0,0,7,2,0,0,2,2),
            arrayOf(2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,7,2,0,13,2,2),
            arrayOf(2,0,0,0,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,15,0,0,7,2,0,0,2,2),
            arrayOf(2,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,0,0,0,7,2,0,9,2,2),
            arrayOf(2,0,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,0,0,0,0,7,2,0,0,2,2),
            arrayOf(2,0,0,0,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,2,2,0,0,2,2),
            arrayOf(2,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,2,2),
            arrayOf(2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,2),
            arrayOf(2,0,0,0,0,0,10,13,0,13,0,13,0,13,0,13,0,13,0,13,0,0,0,0,0,0,0,0,7,2),
            arrayOf(2,2,2,2,2,2,2,4,2,4,2,4,2,4,2,4,2,4,2,4,2,2,2,2,2,2,4,4,2,2),
            arrayOf(2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2)
        )
    }

    private fun loadLevelFour(): Array<Array<Int>> {
        return arrayOf(
            arrayOf(2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
            arrayOf(2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(2,0,0,0,0,0,0,13,13,0,0,0,0,0,13,13,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(2,2,2,2,2,2,2,0,0,2,2,2,2,2,0,0,2,2,2,2,2,0,0,2,2,2,2,2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(2,0,0,0,0,0,2,0,0,2,6,6,6,2,0,0,2,6,6,6,2,0,0,2,2,2,2,2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(2,0,0,0,0,0,2,0,0,2,0,0,0,2,0,0,2,0,0,0,2,0,0,2,2,2,2,2,2,0,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(2,0,0,0,0,0,2,0,0,2,0,0,0,2,0,0,2,0,0,0,2,0,0,2,2,2,8,0,0,0,0,0,9,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(2,0,0,0,0,0,2,0,0,2,0,0,0,2,0,0,2,0,0,0,2,0,0,2,2,2,2,2,0,0,0,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(2,0,0,0,0,0,2,0,0,2,0,0,0,2,0,0,2,0,0,0,2,0,0,2,2,2,2,2,0,0,0,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(2,0,0,0,0,0,2,0,0,2,0,0,0,2,0,0,2,0,0,0,2,13,13,2,2,2,2,2,0,0,0,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(2,0,2,0,0,0,2,4,4,2,0,0,0,2,4,4,2,0,0,0,2,13,13,2,2,2,2,2,2,0,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(2,0,2,0,0,0,2,2,2,2,0,0,0,2,2,2,2,0,0,0,2,2,2,2,2,9,0,0,0,0,0,0,0,8,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(2,0,2,0,0,0,2,2,2,2,0,2,0,2,2,2,2,0,2,0,2,2,2,2,2,2,2,2,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(2,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,10,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(2,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(2,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(2,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
        arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        )
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