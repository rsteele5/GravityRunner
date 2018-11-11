package edu.uco.rsteele5.gravityrunner.model

import android.graphics.*
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation.PORTRAIT
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation.LANDSCAPE
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation.REVERSED_PORTRAIT
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation.REVERSED_LANDSCAPE

class Player(image: Bitmap, x: Float, y: Float) : GameEntity(image, x, y) {

    var speedBoost = false

    var currentRotation: Float = 0f
    var currentOrientation: ScreenOrientation = PORTRAIT

    init {
        this.image = image
        collisionBox = RectF(xPos, yPos, width+xPos, height+yPos)
        rotateGavMatrix = Matrix()
        width = 52f
        height = 100f
    }

    override fun getCollidableBox(): RectF {
        return collisionBox
    }

    override fun update(orientation: ScreenOrientation, motionVector: PhysicsVector) {
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

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawRect(collisionBox, paint)
        canvas.drawBitmap(image!!.rotate(currentRotation), null, collisionBox, null)
    }

    private fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    fun getCenter(): Pair<Float, Float> {
        return  if(currentOrientation == PORTRAIT || currentOrientation == REVERSED_PORTRAIT)
            Pair(getX() + (width / 2), getY() + (height / 2))
        else
            Pair(getX() + (height / 2), getY() + (width / 2))
    }


}
