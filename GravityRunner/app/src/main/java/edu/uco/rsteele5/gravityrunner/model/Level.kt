package edu.uco.rsteele5.gravityrunner.model

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import edu.uco.rsteele5.gravityrunner.control.OrientationManager
import edu.uco.rsteele5.gravityrunner.R
import edu.uco.rsteele5.gravityrunner.Renderable
import java.util.concurrent.CopyOnWriteArrayList

const val SPAWN = 1
const val WALL = 2
const val GOAL = 3
const val SPIKES = 4
const val BAT = 5
const val SPEEDBOOST = 6
const val ARMORBOOST = 7
const val COIN = 8

const val TAG_LC = "LC"

class Level(r: Resources, val map: CopyOnWriteArrayList<CopyOnWriteArrayList<Int>>,
            val screenWidth: Float, val screenHeight: Float): Renderable{

    private var spawnLocX = 0
    private var spawnLocY = 0
    var screenCenter = Pair(0f,0f)
    private val tileSize = 100f
    var resources: Resources? = null
    var isSpawnLoaded = false
    var areEntitiesLoaded = false
    var areBoundariesLoaded = false
    var isLoaded = false
    val boundaryObjects = CopyOnWriteArrayList<BoundaryObject>()
    val gameEntities = CopyOnWriteArrayList<GameEntity>()

    init {
        resources = r
        screenCenter = Pair(screenWidth/2f, screenHeight/2f)
    }

    fun createLevel(){
        scanAndSetSpawnLocation()
        scanAndSetGameEntities()
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
        isSpawnLoaded = true
    }

    //Finds and creates the game entities then adds them to the gameEntities array.
    private fun scanAndSetGameEntities(){
        for(y in 0..(map.size - 1)){
            for (x in 0..(map[y].size - 1)){
                when(map[y][x]){
                    GOAL -> {/*TODO: Create Goal and at it to gameEntities*/}
                    SPIKES -> {/*TODO: Create Spikes and at it to gameEntities*/}
                    BAT -> {/*TODO: Create Bat and at it to gameEntities*/}
                    SPEEDBOOST ->{
                        gameEntities.add(SpeedBoost(BitmapFactory.decodeResource(resources, R.drawable.speed_boost),
                            getOffsetX(x), getOffsetY(y)))
                    }
                    ARMORBOOST -> { /*TODO: Create Armor boost and at it to gameEntities*/ }
                    COIN -> { /*TODO: Create Coin and at it to gameEntities*/ }
                }
            }
        }
        areEntitiesLoaded = true
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
        areBoundariesLoaded = true
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

    fun checkLoadStatus(): Boolean{
        isLoaded = isSpawnLoaded && areEntitiesLoaded && areBoundariesLoaded
        return isLoaded
    }

    //Update the contents of the level as long as the level is loaded
    override fun update(orientation: OrientationManager.ScreenOrientation, motionVector: PhysicsVector) {
        if(isLoaded){
            for (entity in gameEntities) { entity.update(orientation, motionVector) }
            for (bound in boundaryObjects) { bound.update(orientation, motionVector) }
        }
    }

    //Draw the level if it is loaded
    override fun draw(canvas: Canvas, paint: Paint) {
        if(isLoaded) {
            for (bound in boundaryObjects) {
                bound.draw(canvas, paint)
            }
            for (entity in gameEntities) {
                entity.draw(canvas, paint)
            }
        }

    }

}