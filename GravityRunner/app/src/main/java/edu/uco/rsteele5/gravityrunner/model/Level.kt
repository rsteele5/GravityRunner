package edu.uco.rsteele5.gravityrunner.model

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
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

const val TAG_LC = "LC"

class Level(r: Resources, val map: CopyOnWriteArrayList<CopyOnWriteArrayList<Int>>,
            val screenWidth: Float, val screenHeight: Float): Renderable{

    var spawnLocX = 0
    var spawnLocY = 0
    var screenCenter = Pair(0f,0f)
    val tileSize = 100f
    var resources: Resources? = null
    var spawnLoaded = false
    var entitiesLoaded = false
    var boundariesLoaded = false
    var loaded = false
    val boundaryObjects = CopyOnWriteArrayList<BoundaryObject>()
    val gameEntitys = CopyOnWriteArrayList<GameEntity>()

    init {
        resources = r
        screenCenter = Pair(screenWidth/2f, screenHeight/2f)
    }

    fun createLevel(){
        scanAndSetSpawnLocation()
        scanAndSetGameEntitys()
        scanAndCreateBoundaryObj()
    }

    //Finds the Spawn location on the map and saves the x,y coordinates.
    private fun scanAndSetSpawnLocation(){
        for(y in 0..(map.size - 1)){
            for (x in 0..(map[y].size - 1)){
                if(map[y][x] == SPAWN){
                        spawnLocX = x
                        spawnLocY = y
                }
            }
        }
        Log.d(TAG_LC, "Spawn: x:$spawnLocX, y:$spawnLocY")
        spawnLoaded = true
    }

    //Finds and creates the game entities then adds them to the gameEntities array.
    private fun scanAndSetGameEntitys(){
        for(y in 0..(map.size - 1)){
            for (x in 0..(map[y].size - 1)){
                when(map[y][x]){
                    GOAL -> {/*TODO: Create Goal and at it to gameEntitys*/}
                    SPIKES -> {/*TODO: Create Spikes and at it to gameEntitys*/}
                    BAT -> {/*TODO: Create Bat and at it to gameEntitys*/}
                    SPEEDBOOST ->{
                        gameEntitys.add(SpeedBoost(BitmapFactory.decodeResource(resources, R.drawable.speed_boost),
                            getOffsetX(x), getOffsetY(y)))
                    }
                }
            }
        }
        entitiesLoaded = true
    }

    //Scans for and creates the boundary objects then adds them to the boundaryObjects array.
    private fun scanAndCreateBoundaryObj(){
        for(y in 0..(map.size - 1)){
            for (x in 0..(map[y].size - 1)){
                when(map[y][x]){
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
        for (i in x..(map[y].size - 1)){
            if(boundType == map[y][i])
                lengthX++
            else
                break
        }

        for (i in y..(map.size - 1)){
            if(boundType == map[i][x])
                lengthY++
            else
                break
        }
        //Compare lengths and create the optimal wall (Horizontal or Vertical)
        if(lengthX >= lengthY){
            for(i in x..(x+lengthX-1)){ map[y][i] = -boundType }
            boundaryObjects.add(Wall(BitmapFactory.decodeResource(resources, R.drawable.stone100x80),
                getOffsetX(x),getOffsetY(y), lengthX))
        }
        else{
            for (i in y..(y+lengthY-1)){ map[i][x] = -boundType }
            boundaryObjects.add(Wall(BitmapFactory.decodeResource(resources, R.drawable.stone100x80),
                getOffsetX(x),getOffsetY(y), lengthY, true))
        }
    }

    private fun getOffsetX(x: Int): Float{ return tileSize * (x - spawnLocX) + screenCenter.first}
    private fun getOffsetY(y: Int): Float{ return tileSize * (y - spawnLocY) + screenCenter.second}

    override fun update(orientation: OrientationManager.ScreenOrientation, motionVector: PhysicsVector) {
        loaded = spawnLoaded && entitiesLoaded && boundariesLoaded
        if(loaded) {
            for (entity in gameEntitys) { entity.update(orientation, motionVector) }
            for (bound in boundaryObjects) { bound.update(orientation, motionVector) }
        }
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        if(loaded) {
            for (bound in boundaryObjects) { bound.draw(canvas, paint) }
            for (entity in gameEntitys) { entity.draw(canvas, paint) }
        }
    }

}