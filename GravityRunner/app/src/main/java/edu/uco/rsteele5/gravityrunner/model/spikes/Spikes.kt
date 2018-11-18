package edu.uco.rsteele5.gravityrunner.model.spikes

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import edu.uco.rsteele5.gravityrunner.control.OrientationManager.ScreenOrientation
import edu.uco.rsteele5.gravityrunner.model.Enemy
import edu.uco.rsteele5.gravityrunner.model.GameEntity
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector

class Spikes (image: Bitmap, animator: SpikesAnimator, x: Float, y: Float, rotation: Float)
    : GameEntity(image, x, y), Enemy {

    var playerClose: Boolean = false
    var animator: SpikesAnimator
    var rotation: Float

    init {
        width = 100f
        height = 100f
        this.rotation = rotation
        collisionBox = RectF(xPos, yPos, width+xPos, height+yPos)
        this.animator = animator
        setAnimation(0)
    }

    override fun update(orientation: ScreenOrientation, motionVector: PhysicsVector) {
        translate(motionVector.getDeltaX(), motionVector.getDeltaY())
        updateCollisionBox()
        image = animator.getCurrentImage()
        animator.update()
    }

    override fun getCollidableBox(): RectF {
        return getTriggerReleaseBox()
    }

    fun getHitBox() : RectF {
        return collisionBox
    }

    fun getTriggerPulledBox(): RectF {
        when (rotation) {
            0f -> return RectF(
                collisionBox.left - 200,
                collisionBox.top - 200,
                collisionBox.right + 200,
                collisionBox.bottom
            )
            90f -> return RectF(
                collisionBox.left,
                collisionBox.top - 200,
                collisionBox.right + 200,
                collisionBox.bottom + 200
            )
            180f -> return RectF(
                collisionBox.left - 200,
                collisionBox.top,
                collisionBox.right + 200,
                collisionBox.bottom + 200
            )
            else -> return RectF(
                collisionBox.left - 200,
                collisionBox.top - 200,
                collisionBox.right,
                collisionBox.bottom + 200
            )
        }
    }

    fun getTriggerReleaseBox(): RectF {
        when (rotation) {
            0f -> return RectF(
                collisionBox.left - 250,
                collisionBox.top - 250,
                collisionBox.right + 250,
                collisionBox.bottom
            )
            90f -> return RectF(
                collisionBox.left,
                collisionBox.top - 250,
                collisionBox.right + 250,
                collisionBox.bottom + 250
            )
            180f -> return RectF(
                collisionBox.left - 250,
                collisionBox.top,
                collisionBox.right + 250,
                collisionBox.bottom + 250
            )
            else -> return RectF(
                collisionBox.left - 250,
                collisionBox.top - 250,
                collisionBox.right,
                collisionBox.bottom + 250
            )
        }
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(image, null, collisionBox, null)
    }

    fun setAnimation(animationIndex: Int) {
        animator.setAnimation(animationIndex)
    }
}