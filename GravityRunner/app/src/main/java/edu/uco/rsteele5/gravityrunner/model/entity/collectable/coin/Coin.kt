package edu.uco.rsteele5.gravityrunner.model.entity.collectable.coin

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import edu.uco.rsteele5.gravityrunner.control.OrientationManager
import edu.uco.rsteele5.gravityrunner.model.entity.collectable.Collectable
import edu.uco.rsteele5.gravityrunner.model.entity.GameEntity
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector
import edu.uco.rsteele5.gravityrunner.model.entity.player.Player

class Coin (image: Bitmap, animator: CoinAnimator, x: Float, y: Float, rotation: Float, worth: Int)
    : GameEntity(image, x, y), Collectable
{
    var animator: CoinAnimator
    var rotation: Float
    private var worth: Int

    init {
        width = 50f
        height = 50f
        this.rotation = rotation
        this.worth = worth
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

    override fun giveTo(player: Player) {
        player.addCoins(worth)
    }
}