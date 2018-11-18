package edu.uco.rsteele5.gravityrunner.model

import android.content.res.Resources
import android.graphics.*
import android.util.Log
import edu.uco.rsteele5.gravityrunner.Control.OrientationManager
import edu.uco.rsteele5.gravityrunner.R
import edu.uco.rsteele5.gravityrunner.Renderable
import edu.uco.rsteele5.gravityrunner.model.coin.Coin
import edu.uco.rsteele5.gravityrunner.model.coin.CoinAnimator
import edu.uco.rsteele5.gravityrunner.model.powerups.armor.Armor
import edu.uco.rsteele5.gravityrunner.model.powerups.armor.ArmorAnimator
import edu.uco.rsteele5.gravityrunner.model.powerups.speedboost.SpeedBoost
import edu.uco.rsteele5.gravityrunner.model.powerups.speedboost.SpeedBoostAnimator
import edu.uco.rsteele5.gravityrunner.model.spikes.SpikesAnimator
import edu.uco.rsteele5.gravityrunner.model.spikes.Spikes
import java.util.concurrent.CopyOnWriteArrayList

const val SPAWN = 1
const val WALL = 2
const val GOAL = 3
const val SPIKES = 4
const val SPIKES_RIGHT = 5
const val SPIKES_DOWN = 6
const val SPIKES_LEFT = 7
const val BAT = 8
const val SPEEDBOOST = 9
const val COIN = 10
const val ARMOR = 11
const val JUMPBOOST = 12

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
                    SPIKES -> {
                        gameEntitys.add(
                            Spikes(
                                BitmapFactory.decodeResource(resources, R.drawable.spikes_down),
                                SpikesAnimator(resources, 0f),
                                getOffsetX(x), getOffsetY(y), 0f
                            )
                        )
                    }
                    SPIKES_RIGHT -> {
                        gameEntitys.add(
                            Spikes(
                                BitmapFactory.decodeResource(resources, R.drawable.spikes_down),
                                SpikesAnimator(resources, 90f),
                                getOffsetX(x), getOffsetY(y), 90f
                            )
                        )
                    }
                    SPIKES_DOWN -> {
                        gameEntitys.add(
                            Spikes(
                                BitmapFactory.decodeResource(resources, R.drawable.spikes_down),
                                SpikesAnimator(resources, 180f),
                                getOffsetX(x), getOffsetY(y),180f
                            )
                        )
                    }
                    SPIKES_LEFT -> {
                        gameEntitys.add(
                            Spikes(
                                BitmapFactory.decodeResource(resources, R.drawable.spikes_down),
                                SpikesAnimator(resources, 270f),
                                getOffsetX(x), getOffsetY(y), 270f
                            )
                        )
                    }
                    BAT -> {/*TODO: Create Bat and at it to gameEntitys*/}
                    SPEEDBOOST -> {
                        gameEntitys.add(
                            SpeedBoost(
                                BitmapFactory.decodeResource(resources, R.drawable.speed_boost),
                                SpeedBoostAnimator(resources),
                                getOffsetX(x), getOffsetY(y)
                            )
                        )
                    }
                    COIN -> {
                        gameEntitys.add(
                            Coin(
                                BitmapFactory.decodeResource(resources, R.drawable.coin_0),
                                CoinAnimator(resources),
                                getOffsetX(x), getOffsetY(y), 270f
                            )
                        )
                    }
                    ARMOR -> {
                        gameEntitys.add(
                            Armor(
                                BitmapFactory.decodeResource(resources, R.drawable.armor_0),
                                ArmorAnimator(resources),
                                getOffsetX(x), getOffsetY(y)
                            )
                        )
                    }
                }
            }
        }
        Log.d("spikes", "$gameEntitys")
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

    private fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
}