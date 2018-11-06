package edu.uco.rsteele5.gravityrunner.model

import android.graphics.*
import android.util.Log
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation
import java.util.concurrent.CopyOnWriteArrayList

class Wall(image: Bitmap, x: Float, y: Float, lengthOf: Int, varticalFlag: Boolean): BoundaryObject(image, x, y) {
    constructor(image: Bitmap, x: Float, y: Float, lengthOf: Int) : this(image,x,y,lengthOf,false)
    constructor(image: Bitmap, x: Float, y: Float) : this(image,x,y,1,false)

    protected var length = 0
    protected var vertical = false
    protected val wallScalars = CopyOnWriteArrayList<RectF>()
    init {
        this.image = image
        length = lengthOf
        vertical = varticalFlag
        //TODO: Make more modular for other boundary objects

        if(vertical) {
            for (i in 0..(length-1)){
                wallScalars.add(RectF(xPos, (80f*i)+yPos, 100f+xPos, (80f*i)+80f+yPos))
            }
            collisionBox = RectF(xPos, yPos, 100f+xPos, (length * 80f) + yPos)
        }
        else {
            for (i in 0..(length-1)){
                wallScalars.add(RectF((100f*i)+xPos, yPos, (100f*i)+100f+xPos, 80f+yPos))
            }
            collisionBox = RectF(xPos, yPos, (100f*length)+xPos, 80f+yPos)
        }
    }

    override fun update(orientation: ScreenOrientation, gravityVector: Triple<Float, Float, Float>) {

    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawRect(collisionBox,paint) // backup in case image does not render
        for (i in 0..(length-1)) {
            canvas.drawBitmap(image, null, wallScalars[i], null)
        }
    }

    override fun getCollidableBox(): RectF {
        return collisionBox
    }
}