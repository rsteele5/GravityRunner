package edu.uco.rsteele5.gravityrunner.model

import android.graphics.*
import edu.uco.rsteele5.gravityrunner.control.OrientationManager.ScreenOrientation
import edu.uco.rsteele5.gravityrunner.control.OrientationManager.ScreenOrientation.PORTRAIT
import edu.uco.rsteele5.gravityrunner.control.OrientationManager.ScreenOrientation.LANDSCAPE
import edu.uco.rsteele5.gravityrunner.control.OrientationManager.ScreenOrientation.REVERSED_LANDSCAPE
import edu.uco.rsteele5.gravityrunner.control.OrientationManager.ScreenOrientation.REVERSED_PORTRAIT


class Player(image: Bitmap, x: Float, y: Float) : GameEntity(image, x, y) {

    private val SPEED_BOOST_TIME: Long = 4000

    var speedBoost = false
    var jumpBoost = false
    var armorBoost = false
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

    fun setArmor(){
        armorBoost = true
    }

    private fun checkPowerUp() {
        if(speedBoost) {
            speedBoostTimer -= (System.currentTimeMillis() - lastClock)
            lastClock = System.currentTimeMillis()
            if(speedBoostTimer < 0) {
                speedBoost = false
            }
        }
        // check other powerups
    }

    override fun updateOrientation(orientation: ScreenOrientation){
        currentOrientation = orientation
        when (currentOrientation){
            ScreenOrientation.PORTRAIT -> {
                currentRotation = 0f
                collisionBox.set(xPos, yPos, width + xPos, height + yPos)
            }
            ScreenOrientation.LANDSCAPE -> {
                currentRotation = 90f
                collisionBox.set(xPos, yPos, height + xPos, width + yPos)
            }
            ScreenOrientation.REVERSED_PORTRAIT -> {
                currentRotation = 180f
                collisionBox.set(xPos, yPos, width + xPos, height + yPos)
            }
            ScreenOrientation.REVERSED_LANDSCAPE -> {
                currentRotation = 270f
                collisionBox.set(xPos, yPos, height + xPos, width + yPos)
            }
        }
    }
}
