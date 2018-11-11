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
        width = 100f
        height = 100f

        if(vertical) {
            for (i in 0..(length-1)){
                wallScalars.add(RectF(xPos, (height*i)+yPos, width+xPos, (height*i)+height+yPos))
            }
            collisionBox = RectF(xPos, yPos, width+xPos, (length * height) + yPos)
        }
        else {
            for (i in 0..(length-1)){
                wallScalars.add(RectF((width*i)+xPos, yPos, (width*i)+width+xPos, height+yPos))
            }
            collisionBox = RectF(xPos, yPos, (width*length)+xPos, height+yPos)
        }
    }

    override fun update(orientation: ScreenOrientation, motionVector: PhysicsVector) {
        translate(motionVector.getDeltaX(), motionVector.getDeltaY())

        if(vertical) {
            for (i in 0..(length-1)){
                wallScalars[i].set(RectF(xPos, (height*i)+yPos, width+xPos, (height*i)+height+yPos))
            }
            collisionBox.set(RectF(xPos, yPos, width+xPos, (length * height) + yPos))
        }
        else {
            for (i in 0..(length-1)){
                wallScalars[i].set(RectF((width*i)+xPos, yPos, (width*i)+width+xPos, height+yPos))
            }
            collisionBox.set(RectF(xPos, yPos, (width*length)+xPos, height+yPos))
        }
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawRect(collisionBox,paint) // backup in case image does not render
        for (i in 0..(length-1)) {
            canvas.drawBitmap(image!!, null, wallScalars[i], null)
        }
    }

    override fun getCollidableBox(): RectF {
        return collisionBox
    }
}