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
    var currentOrientation: ScreenOrientation = PORTRAIT
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
        currentOrientation = orientation
        when (currentOrientation){
            PORTRAIT -> {
                currentRotation = 0f
                collisionBox.set(xPos, yPos, bobWidth + xPos, bobHeight + yPos)
            }
            LANDSCAPE -> {
                currentRotation = 90f
                collisionBox.set(xPos, yPos, bobHeight + xPos, bobWidth + yPos)
            }
            REVERSED_PORTRAIT -> {
                currentRotation = 180f
                collisionBox.set(xPos, yPos, bobWidth + xPos, bobHeight + yPos)
            }
            REVERSED_LANDSCAPE -> {
                currentRotation = 270f
                collisionBox.set(xPos, yPos, bobHeight + xPos, bobWidth + yPos)
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

    fun getCenter(): Pair<Float, Float> {
        return  if(currentOrientation == PORTRAIT || currentOrientation == REVERSED_PORTRAIT)
                    Pair(getX() + (bobWidth / 2), getY() + (bobHeight / 2))
                else
                    Pair(getX() + (bobHeight / 2), getY() + (bobWidth / 2))
    }

    fun debugMove(dx: Float, dy: Float) {
        xPos += dx
        yPos += dy
    }
}
