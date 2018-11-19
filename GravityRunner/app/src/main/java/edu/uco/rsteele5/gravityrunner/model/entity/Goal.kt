package edu.uco.rsteele5.gravityrunner.model.entity

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import edu.uco.rsteele5.gravityrunner.control.OrientationManager
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector

class Goal(image: Bitmap, x: Float, y: Float, rotation: Float)
    : GameEntity(image, x, y) {

    init {
        width = 50f
        height = 50f
        collisionBox = RectF(xPos, yPos, xPos + width, yPos + height)
    }

    override fun update(orientation: OrientationManager.ScreenOrientation, motionVector: PhysicsVector) {
        translate(motionVector.getDeltaX(), motionVector.getDeltaY())
        updateCollisionBox()
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(image, null, collisionBox, null)
    }
}