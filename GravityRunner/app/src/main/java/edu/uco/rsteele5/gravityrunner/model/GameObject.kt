package edu.uco.rsteele5.gravityrunner.model

import android.graphics.Bitmap
import android.graphics.RectF
import edu.uco.rsteele5.gravityrunner.Renderable

abstract class GameObject(image: Bitmap): CollisionBox, Renderable {

    protected var xPos: Float = 0f
    protected var yPos: Float = 0f
    protected var deltaX: Float = 0f
    protected var deltaY: Float = 0f
    protected var width: Float = 0f
    protected var height: Float = 0f
    protected var collisionBox = RectF()
    protected var image: Bitmap? = null

    init {
        this.image = image
    }

    fun translate(dX: Float, dY: Float) {
        xPos += dX
        yPos += dY
    }

    fun getX(): Float {
        return xPos
    }

    fun getY(): Float {
        return yPos
    }

    fun getImg(): Bitmap? {
        return image
    }

    fun setImg(image: Bitmap) {
        this.image = image
    }


}