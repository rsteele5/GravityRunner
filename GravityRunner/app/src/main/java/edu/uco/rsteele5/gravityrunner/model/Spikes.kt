package edu.uco.rsteele5.gravityrunner.model

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import edu.uco.rsteele5.gravityrunner.Control.OrientationManager.ScreenOrientation

class Spikes (image: Bitmap, x: Float, y: Float) : GameEntity(image, x, y), Enemy{

    init {
        this.image = image
        width = 100f
        height = 100f
        collisionBox = RectF(xPos, yPos, width+xPos, height+yPos)
    }

    override fun update(orientation: ScreenOrientation, motionVector: PhysicsVector) {
        translate(motionVector.getDeltaX(), motionVector.getDeltaY())
        updateOrientation(orientation)
    }

    override fun getCollidableBox(): RectF {
        return collisionBox
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(image!!.rotate(currentRotation), null, collisionBox, null)
    }
}