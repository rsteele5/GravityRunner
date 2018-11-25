package edu.uco.rsteele5.gravityrunner.model.entity

import android.graphics.*
import edu.uco.rsteele5.gravityrunner.control.OrientationManager.ScreenOrientation
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector
import kotlin.math.cos


class Player(image: Bitmap, x: Float, y: Float) : GameEntity(image, x, y) {

    private val SPEED_BOOST_TIME: Long = 4000

    var speedBoost = false
    var jumpBoost = false
    private var speedBoostTimer: Long = 0
    private var lastClock: Long = 0
    private var hitPoints = 1
    private var coins = 0
    private var costume: Bitmap? = null

    init {
        width = 52f
        height = 100f
        collisionBox = RectF(xPos, yPos, width+xPos, height+yPos)
    }


    override fun update(orientation: ScreenOrientation, motionVector: PhysicsVector) {
        updateOrientation(orientation)
        checkPowerUp()
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        super.draw(canvas, paint)
        if (costume != null) {
                canvas.drawBitmap(costume!!.rotate(currentRotation), null, collisionBox, null)
        }
    }

    fun setCostume(newCostume: Bitmap){
        costume = newCostume
    }

    fun setSpeedBoost(){
        speedBoost = true
        speedBoostTimer = SPEED_BOOST_TIME
        lastClock = System.currentTimeMillis()
    }

    fun setArmor(){
        if(hitPoints == 1){
            hitPoints++
        }
    }

    fun getCoins(): Int{
        return coins
    }
    fun addCoins(amount: Int){
        coins += amount
    }
    fun resetCoins(){
        coins = 0
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

    fun getHitPoint(): Int {
        return hitPoints
    }
    fun decrementHitPoints(){
        hitPoints--
    }
    fun resetHitPoints(){
        hitPoints = 1
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
