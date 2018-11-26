package edu.uco.rsteele5.gravityrunner.model.entity.powerups.jumpboost

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import edu.uco.rsteele5.gravityrunner.control.OrientationManager
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector
import edu.uco.rsteele5.gravityrunner.model.entity.Animator
import edu.uco.rsteele5.gravityrunner.model.entity.GameEntity
import edu.uco.rsteele5.gravityrunner.model.entity.player.Player
import edu.uco.rsteele5.gravityrunner.model.entity.powerups.PowerUp

class JumpBoost(image: Bitmap, animator: Animator, x: Float, y: Float) : GameEntity(image, x, y),
    PowerUp
{

    var animator: Animator

    init {
        width = 80f
        height = 80f
        collisionBox = RectF(xPos, yPos, width+xPos, height+yPos)
        this.animator = animator
    }

    override fun update(orientation: OrientationManager.ScreenOrientation, motionVector: PhysicsVector) {
        translate(motionVector.getDeltaX(), motionVector.getDeltaY())
        updateCollisionBox()
        image = animator.getCurrentFrame()
        animator.update()
    }

    override fun applyPowerUp(player: Player) {
        player.setJumpBoost()
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(image, null, collisionBox, null)
    }
}