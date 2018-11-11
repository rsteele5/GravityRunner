package edu.uco.rsteele5.gravityrunner.model

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import edu.uco.rsteele5.gravityrunner.OrientationManager
import edu.uco.rsteele5.gravityrunner.R
import edu.uco.rsteele5.gravityrunner.Renderable
import java.util.concurrent.CopyOnWriteArrayList

const val SPAWN = 1
const val WALL = 2
const val GOAL = 3
const val SPIKES = 4
const val BAT = 5
const val SPEEDBOOST = 6

class Level(r: Resources, basicMap: CopyOnWriteArrayList<CopyOnWriteArrayList<Int>>,
            val screenWidth: Float, val screenHeight: Float): Renderable{

    var spawnLocX = 0
    var spawnLocY = 0
    val tileSize = 100f
    var resources: Resources? = null
    val map = CopyOnWriteArrayList<CopyOnWriteArrayList<Int>>()
    var spawnLoaded = false
    var entitiesLoaded = false
    var boundariesLoaded = false
    var loaded = false
    val boundaryObjects = CopyOnWriteArrayList<BoundaryObject>()
    val gameEntitys = CopyOnWriteArrayList<GameEntity>()

    init {
        map.addAllAbsent(basicMap)
        resources = r
    }

    fun createLevel(){
        scanAndSetSpawnLocation()
        scanAndSetGameEntitys()
        scanAndCreateBoundaryObj()

    }

    //Finds the Spawn location on the map and saves the x,y coordinates.
    private fun scanAndSetSpawnLocation(){
        for(x in 0..(map.size - 1)){
            for (y in 0..(map[x].size - 1)){
                if(map[x][y] == SPAWN){
                        spawnLocX = x
                        spawnLocY = y
                }
            }
        }
        spawnLoaded = true;
    }

    //Finds and creates the game entities then adds them to the gameEntities array.
    private fun scanAndSetGameEntitys(){
        for(x in 0..(map.size - 1)){
            for (y in 0..(map[x].size - 1)){
                when(map[x][y]){
                    GOAL -> {/*TODO: Create Goal and at it to gameEntitys*/}
                    SPIKES -> {/*TODO: Create Spikes and at it to gameEntitys*/}
                    BAT -> {/*TODO: Create Bat and at it to gameEntitys*/}
                    SPEEDBOOST ->{/*TODO: Create SpeedBoost and at it to gameEntitys*/}
                }
            }
        }
        entitiesLoaded = true
    }

    //Scans for and creates the boundary objects then adds them to the boundaryObjects array.
    private fun scanAndCreateBoundaryObj(){
        for(x in 0..(map.size - 1)){
            for (y in 0..(map[x].size - 1)){
                when(map[x][y]){
                    WALL -> {
                        createBestWallFromPoint(WALL, x,y)
                    }
                }
            }
        }
        boundariesLoaded = true
    }

    //Given a starting point (x,y) on the map, creates the best boundary object starting from that point.
    private fun createBestWallFromPoint(boundType: Int, x: Int, y: Int){
        var lengthX = 0
        var lengthY = 0

        //Find the lengths of the potential walls
        for (i in y..(map[x].size - 1)){
            if(boundType == map[x][i])
                lengthX++
            else
                break
        }
        for (i in x..(map.size - 1)){
            if(boundType == map[i][y])
                lengthY++
            else
                break
        }
        //Compare their length and create the optimal wall
        if(lengthX >= lengthY){
            for(i in y..(y+lengthX-1)){
                map[x][i] = -boundType
            }
            boundaryObjects.add(Wall(BitmapFactory.decodeResource(resources, R.drawable.stone100x80),
                getOffsetX(x),getOffsetY(y), lengthX))
        }
        else{
            for (i in x..(x+lengthY-1)){
                map[i][y] = -boundType
            }
            boundaryObjects.add(Wall(BitmapFactory.decodeResource(resources, R.drawable.stone100x80),
                getOffsetX(x),getOffsetY(y), lengthY, true))
        }
    }

    private fun getOffsetX(x: Int): Float{ return tileSize * (x - spawnLocX) }
    private fun getOffsetY(y: Int): Float{ return tileSize * (y - spawnLocY) }

    override fun update(orientation: OrientationManager.ScreenOrientation, motionVector: PhysicsVector) {
        loaded = spawnLoaded && entitiesLoaded && boundariesLoaded
        if(loaded) {
            for (entity in gameEntitys) {
                entity.update(orientation, motionVector)
            }

            for (bound in boundaryObjects) {
                bound.update(orientation, motionVector)
            }
        }
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        if(loaded) {
            for (bound in boundaryObjects) {
                bound.draw(canvas, paint)
            }

            for (entity in gameEntitys) {
                entity.draw(canvas, paint)
            }
        }
    }

}