package edu.uco.rsteele5.gravityrunner.model

import android.graphics.*

import edu.uco.rsteele5.gravityrunner.Control.OrientationManager.ScreenOrientation
import edu.uco.rsteele5.gravityrunner.Control.OrientationManager.ScreenOrientation.*


abstract class GameEntity(image: Bitmap, x: Float, y: Float): GameObject(image) {

    protected var currentRotation: Float = 0f
    protected var currentOrientation: ScreenOrientation = PORTRAIT

    init{
        xPos = x
        yPos = y
    }

    protected fun updateOrientation(orientation: ScreenOrientation){
        currentOrientation = orientation
        when (currentOrientation){
            PORTRAIT -> {
                currentRotation = 0f
                collisionBox.set(xPos, yPos, width + xPos, height + yPos)
            }
            LANDSCAPE -> {
                currentRotation = 90f
                collisionBox.set(xPos, yPos, height + xPos, width + yPos)
            }
            REVERSED_PORTRAIT -> {
                currentRotation = 180f
                collisionBox.set(xPos, yPos, width + xPos, height + yPos)
            }
            REVERSED_LANDSCAPE -> {
                currentRotation = 270f
                collisionBox.set(xPos, yPos, height + xPos, width + yPos)
            }
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