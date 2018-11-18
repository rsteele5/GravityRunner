package edu.uco.rsteele5.gravityrunner.model.powerups.armor

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import edu.uco.rsteele5.gravityrunner.control.OrientationManager
import edu.uco.rsteele5.gravityrunner.model.GameEntity
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector
import edu.uco.rsteele5.gravityrunner.model.Player
import edu.uco.rsteele5.gravityrunner.model.PowerUp

class Armor(image: Bitmap, animator: ArmorAnimator, x: Float, y: Float) : GameEntity(image, x, y), PowerUp {

    var animator: ArmorAnimator

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
        player.setArmor()
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(image, null, collisionBox, null)
    }
}