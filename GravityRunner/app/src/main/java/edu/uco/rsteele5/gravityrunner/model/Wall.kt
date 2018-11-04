package edu.uco.rsteele5.gravityrunner.model

import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import edu.uco.rsteele5.gravityrunner.GameEngine
import edu.uco.rsteele5.gravityrunner.R
import java.util.concurrent.CopyOnWriteArrayList

class Wall(engine: GameEngine, x: Float, y: Float, lengthOf: Int, varticalFlag: Boolean): BoundaryObject(engine, x, y) {
    constructor(engine: GameEngine, x: Float, y: Float, lengthOf: Int) : this(engine,x,y,lengthOf,false)
    constructor(engine: GameEngine, x: Float, y: Float) : this(engine,x,y,1,false)

    protected var length = 0
    protected var vertical = false
    protected val wallScalars = CopyOnWriteArrayList<RectF>()
    init {
        image = BitmapFactory.decodeResource(engine.resources, R.drawable.stone100x80)
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

    override fun update() {

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