package edu.uco.rsteele5.gravityrunner.model

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation

abstract class GameObject(image: Bitmap): CollisionBox {

    protected var xPos: Float = 0f
    protected var yPos: Float = 0f
    protected var deltaX: Float = 0f
    protected var deltaY: Float = 0f
    protected var collisionBox = RectF()
    protected var image: Bitmap? = null

    init {
        this.image = image
    }

    abstract fun update(orientation: ScreenOrientation, gravityVector: Triple<Float, Float, Float>)
    abstract fun draw(canvas: Canvas, paint: Paint)

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