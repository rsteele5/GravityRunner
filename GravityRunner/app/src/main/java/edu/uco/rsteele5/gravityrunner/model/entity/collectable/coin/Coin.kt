package edu.uco.rsteele5.gravityrunner.model.entity.coin

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import edu.uco.rsteele5.gravityrunner.control.OrientationManager
import edu.uco.rsteele5.gravityrunner.model.entity.GameEntity
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

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(image!!.rotate(currentRotation), null, collisionBox, null)
    }
}