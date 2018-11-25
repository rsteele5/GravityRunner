package edu.uco.rsteele5.gravityrunner.model.entity.enemy.spikes

import android.graphics.*
import edu.uco.rsteele5.gravityrunner.control.OrientationManager.ScreenOrientation
import edu.uco.rsteele5.gravityrunner.model.entity.enemy.Enemy
import edu.uco.rsteele5.gravityrunner.model.entity.GameEntity
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector

class Spikes (image: Bitmap, animator: SpikesAnimator, x: Float, y: Float, rotation: Float)
    : GameEntity(image, x, y), Enemy {

    var animator: SpikesAnimator
    var rotation: Float

    init {
        width = 60f
        height = 60f
        this.rotation = rotation
        collisionBox = RectF(xPos, yPos, width+xPos, height+yPos)
        moveSpikesToFloor()
        this.animator = animator
        setAnimation(0)
    }

    override fun update(orientation: ScreenOrientation, motionVector: PhysicsVector) {
        translate(motionVector.getDeltaX(), motionVector.getDeltaY())
        updateCollisionBox()
        image = animator.getCurrentFrame()
        animator.update()
    }

    private fun moveSpikesToFloor() {
        when (rotation) {
            0f -> yPos += 40
            270f -> xPos += 40
        }
    }

    override fun getCollidableBox(): RectF {
        return getTriggerReleaseBox()
    }

    fun getHitBox() : RectF {
        return RectF(xPos, yPos, width+xPos-20,height+yPos-20)
    }

    fun getTriggerPulledBox(): RectF {
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

    fun getTriggerReleaseBox(): RectF {
        when (rotation) {
            0f -> return RectF(
                collisionBox.left - 300,
                collisionBox.top - 300,
                collisionBox.right + 300,
                collisionBox.bottom
            )
            90f -> return RectF(
                collisionBox.left,
                collisionBox.top - 300,
                collisionBox.right + 300,
                collisionBox.bottom + 300
            )
            180f -> return RectF(
                collisionBox.left - 300,
                collisionBox.top,
                collisionBox.right + 300,
                collisionBox.bottom + 300
            )
            else -> return RectF(
                collisionBox.left - 300,
                collisionBox.top - 300,
                collisionBox.right,
                collisionBox.bottom + 300
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