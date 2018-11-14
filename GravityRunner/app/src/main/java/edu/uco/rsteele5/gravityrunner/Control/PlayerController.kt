package edu.uco.rsteele5.gravityrunner.Control

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import edu.uco.rsteele5.gravityrunner.Renderable
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector
import edu.uco.rsteele5.gravityrunner.model.Player

class PlayerController(image: Bitmap, val screenWidth: Float, val screenHeight: Float): Renderable{

    var player: Player? = null
    private val speed = 10f

    init {
        player = Player(image, screenWidth/2f, screenHeight/2f)
    }

    override fun update(orientation: OrientationManager.ScreenOrientation, motionVector: PhysicsVector) {
        player!!.update(orientation,motionVector)
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        player!!.draw(canvas,paint)
    }

    fun getSpeed(): Float{
        return speed * getSpeedModifer()
    }

    private fun getSpeedModifer(): Float{
        return if(player!!.speedBoost) 3f else 1f
    }

}