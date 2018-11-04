package edu.uco.rsteele5.gravityrunner.model

import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import edu.uco.rsteele5.gravityrunner.*


class BitmapBob(engine: GameEngine, x: Float, y: Float) : GameEntity(engine, x, y) {

    val bobWidth = 56f
    val bobHeight = 86f
    var isMoving = false

    var boundTop = false
    var boundBottom = false
    var boundLeft = false
    var boundRight = false

    init {
        isMoving = false
        image = BitmapFactory.decodeResource(engine.resources, R.drawable.bob)
        deltaX = 1f
        deltaY = 3f
        collisionBox = RectF(xPos, yPos, bobWidth+xPos, bobHeight+yPos)
    }

    override fun getCollidableBox(): RectF {
        return collisionBox
    }

    override fun update() {
        Log.d(TAG_GR, isMoving.toString())
        when (engine!!.orientation) {
            OrientationManager.ScreenOrientation.PORTRAIT -> {
                if (deltaX >= 0 && deltaY <= 0) {
                    var temp = deltaX
                    deltaX = deltaY * -1
                    deltaY = temp
                }
                else if (deltaX <= 0 && deltaY >= 0) {
                    var temp = deltaX
                    deltaX = deltaY
                    deltaY = temp * -1
                }
            }
            OrientationManager.ScreenOrientation.LANDSCAPE -> {
                if (deltaX >= 0 && deltaY >= 0) {
                    var temp = deltaX
                    deltaX = deltaY * -1
                    deltaY = temp
                }
                else if (deltaX <= 0 && deltaY <= 0) {
                    var temp = deltaX
                    deltaX = deltaY
                    deltaY = temp * -1
                }
            }
            OrientationManager.ScreenOrientation.REVERSED_PORTRAIT -> {
                if (deltaX <= 0 && deltaY >= 0) {
                    var temp = deltaX
                    deltaX = deltaY * -1
                    deltaY = temp
                }
                else if (deltaX > 0) {
                    var temp = deltaX
                    deltaX = deltaY * -1
                    deltaY = temp
                }
            }
            OrientationManager.ScreenOrientation.REVERSED_LANDSCAPE -> {
                if (deltaX <= 0 && deltaY <= 0) {
                    var temp = deltaX
                    deltaX = deltaY * -1
                    deltaY = temp
                }
                else if (deltaX >= 0 && deltaY >= 0) {
                    var temp = deltaX
                    deltaX = deltaY
                    deltaY = temp * -1
                }
            }
        }

        xPos += deltaX
        yPos += deltaY
        collisionBox.set(xPos,yPos,bobWidth+xPos, bobHeight+yPos)

        Log.d(TAG_GR, "Ori: ${engine!!.orientation.name} deltaX: $deltaX")
        Log.d(TAG_GR, "Ori: ${engine!!.orientation.name} deltaY: $deltaY")

    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(
            image,null, collisionBox,null)
    }


}
