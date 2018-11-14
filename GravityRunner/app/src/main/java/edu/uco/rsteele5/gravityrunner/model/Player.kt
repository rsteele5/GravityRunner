package edu.uco.rsteele5.gravityrunner.model

import android.graphics.*
import android.util.Log
import edu.uco.rsteele5.gravityrunner.Control.OrientationManager.ScreenOrientation


class Player(image: Bitmap, x: Float, y: Float) : GameEntity(image, x, y) {

    private val SPEED_BOOST_TIME: Long = 4000

    var speedBoost = false
    private var speedBoostTimer: Long = 0
    private var lastClock: Long = 0

    init {
        width = 52f
        height = 100f
        collisionBox = RectF(xPos, yPos, width+xPos, height+yPos)
    }


    override fun update(orientation: ScreenOrientation, motionVector: PhysicsVector) {
        updateOrientation(orientation)
        checkPowerUp()
    }

    fun setSpeedBoost(){
        speedBoost = true
        speedBoostTimer = SPEED_BOOST_TIME
        lastClock = System.currentTimeMillis()
    }

    private fun checkPowerUp() {
        if(speedBoost) {
            speedBoostTimer -= (System.currentTimeMillis() - lastClock)
            lastClock = System.currentTimeMillis()
            Log.d("sb", "speedBoost: ${speedBoostTimer}")
            if(speedBoostTimer < 0) {
                speedBoost = false
            }
        }
        // check other powerups
    }

}
