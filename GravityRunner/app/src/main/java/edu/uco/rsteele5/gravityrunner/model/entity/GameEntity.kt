package edu.uco.rsteele5.gravityrunner.model.entity

import android.graphics.*

import edu.uco.rsteele5.gravityrunner.control.OrientationManager.ScreenOrientation
import edu.uco.rsteele5.gravityrunner.control.OrientationManager.ScreenOrientation.*
import edu.uco.rsteele5.gravityrunner.model.GameObject
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector


abstract class GameEntity(image: Bitmap, x: Float, y: Float): GameObject(image) {

    protected var currentRotation: Float = 0f
    protected var currentOrientation: ScreenOrientation = PORTRAIT
    //protected lateinit var animator: Animator

    init{
        xPos = x
        yPos = y
    }

    protected open fun updateCollisionBox() {
        collisionBox.set(xPos, yPos, width + xPos, height + yPos)
    }

    protected open fun updateOrientation(orientation: ScreenOrientation){
        currentOrientation = orientation
        currentRotation = when (currentOrientation){
            PORTRAIT -> 0f
            LANDSCAPE -> 90f
            REVERSED_PORTRAIT -> 180f
            REVERSED_LANDSCAPE -> 270f
        }
    }

    protected fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    fun getCenter(): Pair<Float, Float> {
        return  if(currentOrientation == PORTRAIT || currentOrientation == REVERSED_PORTRAIT)
            Pair(getX() + (width / 2), getY() + (height / 2))
        else
            Pair(getX() + (height / 2), getY() + (width / 2))
    }

    override fun getCollidableBox(): RectF {
        return collisionBox
    }

    override fun update(orientation: ScreenOrientation, motionVector: PhysicsVector) {
        translate(motionVector.getDeltaX(), motionVector.getDeltaY())
        updateOrientation(orientation)
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(image!!.rotate(currentRotation), null, collisionBox, null)
    }
}