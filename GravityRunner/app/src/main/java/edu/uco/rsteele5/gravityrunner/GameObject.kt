package edu.uco.rsteele5.gravityrunner

import android.graphics.Bitmap
import android.graphics.BitmapFactory

abstract class GameObject(engine: GameEngine) {
    protected var engine: GameEngine? = null
    protected var xPos: Float = 0f
    protected var yPos: Float = 0f
    protected var deltaX: Float = 0f
    protected var deltaY: Float = 0f

    protected var image: Bitmap? = null

    init {
        this.engine = engine
    }

    abstract fun update()
    abstract fun draw()

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