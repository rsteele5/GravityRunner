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

    var onGround = false

    var speed = 8f
    val portraitRight = Pair(speed, 0f)
    val landscapeRight = Pair(0f, speed)
    val reversePortraitRight = Pair(-speed,0f)
    val reverseLandscapeRight = Pair(0f, -speed)

    init {
        isMoving = false
        image = BitmapFactory.decodeResource(engine.resources, R.drawable.bob)
        deltaX = 0.001f
        deltaY = 2f
        collisionBox = RectF(xPos, yPos, bobWidth+xPos, bobHeight+yPos)
    }

    override fun getCollidableBox(): RectF {
        return collisionBox
    }

    override fun update() {
        Log.d(TAG_GR, isMoving.toString())
        when (engine!!.orientation) {
            OrientationManager.ScreenOrientation.PORTRAIT -> {
                if(!onGround) {
                    if (deltaX >= 0 && deltaY <= 0) {
                        var temp = deltaX
                        deltaX = deltaY * -1
                        deltaY = temp
                    } else if (deltaX < 0 && deltaY > 0) {
                        var temp = deltaX
                        deltaX = deltaY
                        deltaY = temp * -1
                    }
                }else {
                    deltaX = portraitRight.first
                    deltaY = portraitRight.second
                }
            }
            OrientationManager.ScreenOrientation.LANDSCAPE -> {
                if(!onGround) {
                    if (deltaX >= 0 && deltaY >= 0) {
                        var temp = deltaX
                        deltaX = deltaY * -1
                        deltaY = temp
                    } else if (deltaX < 0 && deltaY < 0) {
                        var temp = deltaX
                        deltaX = deltaY
                        deltaY = temp * -1
                    }
                }else{
                    deltaX = landscapeRight.first
                    deltaY = landscapeRight.second
                }
            }
            OrientationManager.ScreenOrientation.REVERSED_PORTRAIT -> {
                if(!onGround) {
                    if (deltaX <= 0 && deltaY >= 0) {
                        var temp = deltaX
                        deltaX = deltaY * -1
                        deltaY = temp
                    } else if (deltaX > 0) {
                        var temp = deltaX
                        deltaX = deltaY * -1
                        deltaY = temp
                    }
                }else{
                    deltaX = reversePortraitRight.first
                    deltaY = reversePortraitRight.second
                }
            }
            OrientationManager.ScreenOrientation.REVERSED_LANDSCAPE -> {
                if(!onGround) {
                    if (deltaX <= 0 && deltaY <= 0) {
                        var temp = deltaX
                        deltaX = deltaY * -1
                        deltaY = temp
                    } else if (deltaX > 0 && deltaY > 0) {
                        var temp = deltaX
                        deltaX = deltaY
                        deltaY = temp * -1
                    }
                }else{
                    deltaX = reverseLandscapeRight.first
                    deltaY = reverseLandscapeRight .second
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
        canvas.drawRect(collisionBox,paint) // backup in case image does not render
        canvas.drawBitmap(image,null, collisionBox,null)
    }


}
