package edu.uco.rsteele5.gravityrunner.control

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import edu.uco.rsteele5.gravityrunner.Renderable
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector
import edu.uco.rsteele5.gravityrunner.model.Player

class PlayerController(image: Bitmap, screenWidth: Float, screenHeight: Float): Renderable{

    var player: Player? = null
    private var previousOrientation = OrientationManager.ScreenOrientation.PORTRAIT
    private val speed = 10f
    private val jumpSpeed = 25f
    private val speedDeprecator = 1f
    private var jumpVector = PhysicsVector()
    private var runVector = PhysicsVector()

    init {
        player = Player(image, screenWidth/2f, screenHeight/2f)
    }

    override fun update(orientation: OrientationManager.ScreenOrientation, motionVector: PhysicsVector) {
        if(jumpVector.magnitude > 0f)
            jumpVector.deprecateMagnitudeBy(speedDeprecator)

        if(previousOrientation != orientation) {
            jumpVector.zero()
            runVector.zero()
        }

        player!!.update(orientation,motionVector)
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        player!!.draw(canvas,paint)
    }

    fun getRunningVector(): PhysicsVector{
        return runVector
    }
    fun getJumpingVector(): PhysicsVector{
        return jumpVector
    }

    private fun setRunningVector(orientation: OrientationManager.ScreenOrientation): PhysicsVector{
        return when(orientation){
            OrientationManager.ScreenOrientation.PORTRAIT -> PhysicsVector(-1f, 0f, 1f)
            OrientationManager.ScreenOrientation.LANDSCAPE -> PhysicsVector(0f, -1f, 1f)
            OrientationManager.ScreenOrientation.REVERSED_PORTRAIT -> PhysicsVector(1f, 0f, 1f)
            OrientationManager.ScreenOrientation.REVERSED_LANDSCAPE -> PhysicsVector(0f, 1f, 1f)
        }
    }

    private fun setJumpingVector(orientation: OrientationManager.ScreenOrientation): PhysicsVector{
        return when(orientation){
            OrientationManager.ScreenOrientation.PORTRAIT -> PhysicsVector(0f, 1f, getJumpSpeed())
            OrientationManager.ScreenOrientation.LANDSCAPE -> PhysicsVector(-1f, 0f, getJumpSpeed())
            OrientationManager.ScreenOrientation.REVERSED_PORTRAIT -> PhysicsVector(0f, -1f, getJumpSpeed())
            OrientationManager.ScreenOrientation.REVERSED_LANDSCAPE -> PhysicsVector(1f, 0f, getJumpSpeed())
        }
    }

    fun startJump(orientation: OrientationManager.ScreenOrientation){
        jumpVector = setJumpingVector(orientation)
    }
    fun stopJump(){
        jumpVector.zero()
    }

    fun startRun(orientation: OrientationManager.ScreenOrientation){
        if(runVector.magnitude == 0f)
            runVector = setRunningVector(orientation)
    }
    fun depricateRun(){
        runVector.deprecateMagnitudeBy(speedDeprecator/10f)
    }

    fun incrementRun() {
        runVector.incrementMagnitudeUpTo(getSpeedModifier(), getSpeed())
    }

    fun getSpeed(): Float{
        return speed * getSpeedModifier()
    }
    fun getJumpSpeed(): Float{
        return jumpSpeed * getJumpSpeedModifier()
    }

    private fun getSpeedModifier(): Float{
        return if(player!!.speedBoost) 3f else 1f
    }
    private fun getJumpSpeedModifier(): Float{
        return if(player!!.jumpBoost) 2f else 1f
    }
}