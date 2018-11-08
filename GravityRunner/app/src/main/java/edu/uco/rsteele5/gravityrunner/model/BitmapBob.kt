package edu.uco.rsteele5.gravityrunner.model

import android.graphics.*
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation.PORTRAIT
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation.LANDSCAPE
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation.REVERSED_PORTRAIT
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation.REVERSED_LANDSCAPE

class BitmapBob(image: Bitmap, x: Float, y: Float) : GameEntity(image, x, y) {

    val bobWidth = 60f
    val bobHeight = 114f
    var isMoving = false

    var onGround = false
    var currentRotation: Float = 0f

    var speed = 3f

    init {
        isMoving = false
        this.image = image
        collisionBox = RectF(xPos, yPos, bobWidth+xPos, bobHeight+yPos)
        rotateGavMatrix = Matrix()
    }

    override fun getCollidableBox(): RectF {
        return collisionBox
    }

    override fun update(orientation: ScreenOrientation, gravityVector: Triple<Float, Float, Float>) {
        currentRotation = when (orientation){
            PORTRAIT -> 0f
            LANDSCAPE -> 90f
            REVERSED_PORTRAIT -> 180f
            REVERSED_LANDSCAPE -> 270f
        }

        when (currentRotation) {
            0f -> collisionBox.set(xPos, yPos, bobWidth + xPos, bobHeight + yPos)
            90f -> collisionBox.set(xPos, yPos, bobHeight + xPos, bobWidth + yPos)
            180f -> collisionBox.set(xPos, yPos, bobWidth + xPos, bobHeight + yPos)
            270f -> collisionBox.set(xPos, yPos, bobHeight + xPos, bobWidth + yPos)
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
