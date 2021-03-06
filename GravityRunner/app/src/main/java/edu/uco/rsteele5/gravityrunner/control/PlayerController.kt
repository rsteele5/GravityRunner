package edu.uco.rsteele5.gravityrunner.control

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import edu.uco.rsteele5.gravityrunner.model.Renderable
import edu.uco.rsteele5.gravityrunner.control.OrientationManager.ScreenOrientation
import edu.uco.rsteele5.gravityrunner.control.OrientationManager.ScreenOrientation.PORTRAIT
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector
import edu.uco.rsteele5.gravityrunner.model.entity.player.Player
import edu.uco.rsteele5.gravityrunner.model.entity.player.PlayerAnimator

class PlayerController(image: Bitmap, private var animator: PlayerAnimator, screenWidth: Float, screenHeight: Float)
    : Renderable {

    var player: Player? = null
    private var previousOrientation = PORTRAIT
    private val speed = 13f
    private val jumpSpeed = 35f
    private val speedDeprecator = 1.5f
    private var jumpVector = PhysicsVector()
    private var runVector = PhysicsVector()

    init {
        player = Player(image, screenWidth / 2f, screenHeight / 2f)
    }

    override fun update(orientation: ScreenOrientation, motionVector: PhysicsVector) {
        if(jumpVector.magnitude > 0f)
            jumpVector.deprecateMagnitudeBy(speedDeprecator)

        if(previousOrientation != orientation) {
            jumpVector.zero()
            runVector.zero()
        }

        player!!.update(orientation,motionVector)
        previousOrientation = orientation
        animator.update()
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        player!!.setImg(animator.getCurrentFrame())
        player!!.draw(canvas,paint)
    }

    fun setCostume(costume: Bitmap?){
        if(costume != null)
            player!!.setCostume(costume)
    }

    private fun setRunningVector(orientation: ScreenOrientation): PhysicsVector{
        return when(orientation){
            PORTRAIT -> PhysicsVector(-1f, 0f, 1f)
            ScreenOrientation.LANDSCAPE -> PhysicsVector(0f, -1f, 1f)
            ScreenOrientation.REVERSED_PORTRAIT -> PhysicsVector(1f, 0f, 1f)
            ScreenOrientation.REVERSED_LANDSCAPE -> PhysicsVector(0f, 1f, 1f)
        }
    }
    private fun setJumpingVector(orientation: ScreenOrientation): PhysicsVector{
        return when(orientation){
            PORTRAIT -> PhysicsVector(0f, 1f, getJumpSpeed())
            ScreenOrientation.LANDSCAPE -> PhysicsVector(-1f, 0f, getJumpSpeed())
            ScreenOrientation.REVERSED_PORTRAIT -> PhysicsVector(0f, -1f, getJumpSpeed())
            ScreenOrientation.REVERSED_LANDSCAPE -> PhysicsVector(1f, 0f, getJumpSpeed())
        }
    }

    fun startJump(orientation: ScreenOrientation){
        jumpVector = setJumpingVector(orientation)
    }
    fun stopJump(){
        jumpVector.zero()
    }

    fun startRun(orientation: ScreenOrientation){
        if(runVector.magnitude == 0f){
            runVector = setRunningVector(orientation)
        }

    }
    fun depricateRun(){
        runVector.deprecateMagnitudeBy(speedDeprecator/8f)
    }
    fun incrementRun() {
        runVector.incrementMagnitudeUpTo(getSpeedModifier(), getSpeed())
    }

    fun getRunningVector(): PhysicsVector{
        return runVector
    }
    fun getJumpingVector(): PhysicsVector{
        return jumpVector
    }

    fun getSpeed(): Float{
        return speed * getSpeedModifier()
    }
    fun getJumpSpeed(): Float{
        return jumpSpeed * getJumpSpeedModifier()
    }

    private fun getSpeedModifier(): Float{
        return if(player!!.speedBoost) 2f else 1f
    }
    private fun getJumpSpeedModifier(): Float{
        return if(player!!.jumpBoost) 2f else 1f
    }

    fun getHitPoints(): Int{
        return player!!.getHitPoint()
    }
    fun getCoins(): Int{
        return player!!.getCoins()
    }

    fun reset(){
        player!!.resetHitPoints()
        player!!.resetCoins()
        player!!.speedBoost = false
        jumpVector.zero()
        runVector.zero()
        previousOrientation = PORTRAIT
    }

    fun setAnimation(animationIndex: Int){
        animator.setAnimation(animationIndex)
    }
}