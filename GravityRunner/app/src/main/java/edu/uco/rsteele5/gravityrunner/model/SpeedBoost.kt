package edu.uco.rsteele5.gravityrunner.model

import android.graphics.*
import edu.uco.rsteele5.gravityrunner.control.OrientationManager

class SpeedBoost(image: Bitmap, x: Float, y: Float) : GameEntity(image, x, y) {


    var currentRotation: Float = 0f
    var currentOrientation: OrientationManager.ScreenOrientation = OrientationManager.ScreenOrientation.PORTRAIT

    init {
        width = 90f
        height = 90f
        this.image = image
        collisionBox = RectF(xPos, yPos, width+xPos, height+yPos)
    }

    override fun getCollidableBox(): RectF {
        return collisionBox
    }

    override fun update(orientation: OrientationManager.ScreenOrientation, motionVector: PhysicsVector) {
        translate(motionVector.getDeltaX(), motionVector.getDeltaY())

        currentOrientation = orientation
        when (currentOrientation){
            OrientationManager.ScreenOrientation.PORTRAIT -> {
                currentRotation = 0f
                collisionBox.set(xPos, yPos, width + xPos, height + yPos)
            }
            OrientationManager.ScreenOrientation.LANDSCAPE -> {
                currentRotation = 90f
                collisionBox.set(xPos, yPos, height + xPos, width + yPos)
            }
            OrientationManager.ScreenOrientation.REVERSED_PORTRAIT -> {
                currentRotation = 180f
                collisionBox.set(xPos, yPos, width + xPos, height + yPos)
            }
            OrientationManager.ScreenOrientation.REVERSED_LANDSCAPE -> {
                currentRotation = 270f
                collisionBox.set(xPos, yPos, height + xPos, width + yPos)
            }
        }
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(image!!.rotate(currentRotation), null, collisionBox, null)
    }

    private fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
}