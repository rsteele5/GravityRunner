package edu.uco.rsteele5.gravityrunner.model.coin

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import edu.uco.rsteele5.gravityrunner.control.OrientationManager
import edu.uco.rsteele5.gravityrunner.model.GameEntity
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector

class Coin (image: Bitmap, animator: CoinAnimator, x: Float, y: Float, rotation: Float)
    : GameEntity(image, x, y)
{
    var animator: CoinAnimator
    var rotation: Float

    init {
        width = 50f
        height = 50f
        this.rotation = rotation
        collisionBox = RectF(xPos, yPos, xPos + width, yPos + height)
        this.animator = animator
    }

    override fun update(orientation: OrientationManager.ScreenOrientation, motionVector: PhysicsVector) {
        translate(motionVector.getDeltaX(), motionVector.getDeltaY())
        updateCollisionBox()
        animator.update()
        image = animator.getCurrentImage()
    }

    override fun getCollidableBox(): RectF {
        return collisionBox
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(image!!.rotate(currentRotation), null, collisionBox, null)
    }

    override fun updateOrientation(orientation: OrientationManager.ScreenOrientation){
        currentOrientation = orientation
        when (currentOrientation) {
            OrientationManager.ScreenOrientation.PORTRAIT -> {
                currentRotation = 0f
                collisionBox.set(xPos + 25, yPos + 25, width + xPos + 25, height + yPos + 25)
            }
            OrientationManager.ScreenOrientation.LANDSCAPE -> {
                currentRotation = 90f
                collisionBox.set(xPos + 25, yPos + 25, height + xPos + 25, width + yPos + 25)
            }
            OrientationManager.ScreenOrientation.REVERSED_PORTRAIT -> {
                currentRotation = 180f
                collisionBox.set(xPos + 25, yPos + 25, width + xPos + 25, height + yPos + 25)
            }
            OrientationManager.ScreenOrientation.REVERSED_LANDSCAPE -> {
                currentRotation = 270f
                collisionBox.set(xPos + 25, yPos + 25, height + xPos + 25, width + yPos + 25)
            }
        }
    }
}