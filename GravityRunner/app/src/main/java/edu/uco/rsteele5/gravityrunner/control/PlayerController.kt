package edu.uco.rsteele5.gravityrunner.control

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import edu.uco.rsteele5.gravityrunner.Renderable
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector
import edu.uco.rsteele5.gravityrunner.model.Player

class PlayerController(image: Bitmap, screenWidth: Float, screenHeight: Float): Renderable{

    var player: Player? = null
    private var jumpInfo: Triple<Float, Float, OrientationManager.ScreenOrientation>? = null
    private var jumping = false
    private val speed = 10f

    init {
        player = Player(image, screenWidth/2f, screenHeight/2f)
    }

    override fun update(orientation: OrientationManager.ScreenOrientation, motionVector: PhysicsVector) {
        player!!.update(orientation,motionVector)
        if(jumping && jumpInfo?.third != orientation)
            stopJump()
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        player!!.draw(canvas,paint)
    }

    fun startJump(orientation: OrientationManager.ScreenOrientation){
        jumpInfo = Triple(player!!.getCenter().first, player!!.getCenter().second, orientation)
        jumping = true
    }
    fun stopJump(){
        jumpInfo = null
        jumping = false
    }

    fun getSpeed(): Float{
        return speed * getSpeedModifer()
    }

    private fun getSpeedModifer(): Float{
        return if(player!!.speedBoost) 3f else 1f
    }

}