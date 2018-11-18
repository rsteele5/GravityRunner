package edu.uco.rsteele5.gravityrunner.model.powerups.speedboost

import android.graphics.*
import edu.uco.rsteele5.gravityrunner.Control.OrientationManager
import edu.uco.rsteele5.gravityrunner.model.GameEntity
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector
import edu.uco.rsteele5.gravityrunner.model.Player
import edu.uco.rsteele5.gravityrunner.model.PowerUp

class SpeedBoost(image: Bitmap, animator: SpeedBoostAnimator, x: Float, y: Float) : GameEntity(image, x, y), PowerUp
{
    var animator: SpeedBoostAnimator

    init {
        width = 80f
        height = 80f
        collisionBox = RectF(xPos, yPos, width+xPos, height+yPos)
        this.animator = animator
    }

    override fun update(orientation: OrientationManager.ScreenOrientation, motionVector: PhysicsVector) {
        translate(motionVector.getDeltaX(), motionVector.getDeltaY())
        updateCollisionBox()
        image = animator.getCurrentImage()
        animator.update()
    }

    override fun applyPowerUp(player: Player) {
        player.setSpeedBoost()
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(image, null, collisionBox, null)
    }
}