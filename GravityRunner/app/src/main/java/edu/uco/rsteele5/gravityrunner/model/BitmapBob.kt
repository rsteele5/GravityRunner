package edu.uco.rsteele5.gravityrunner.model

import android.graphics.*
import android.util.Log
import edu.uco.rsteele5.gravityrunner.*


class BitmapBob(engine: GameEngine, x: Float, y: Float) : GameEntity(engine, x, y) {

    val bobWidth = 86f
    val bobHeight = 86f
    var isMoving = false

    var onGround = false
    var currentOrientation: Int = 0

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
        rotateGavMatrix = Matrix()
        //rotateGavMatrix.setRotate(0f, xPos, yPos)
    }

    override fun getCollidableBox(): RectF {
        return collisionBox
    }

    override fun update() {
        Log.d(TAG_GR, isMoving.toString())
        when (engine!!.orientation) {
            OrientationManager.ScreenOrientation.PORTRAIT -> {
                currentOrientation = 0
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
                currentOrientation = 1
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
                currentOrientation = 2
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
                currentOrientation = 3
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
        rotateGavMatrix.reset()
        rotateGavMatrix.preTranslate(xPos, yPos)

        Log.d(TAG_GR, "Ori: ${engine!!.orientation.name} deltaX: $deltaX")
        Log.d(TAG_GR, "Ori: ${engine!!.orientation.name} deltaY: $deltaY")

    }

    override fun draw(canvas: Canvas, paint: Paint) {
        rotateGavMatrix.reset()
        var rotation = 0f
        Log.d(TAG_GR, "$currentOrientation")
        when(currentOrientation){
            0 -> {
                rotation = 0f
                Log.d(TAG_GR, "Portrait: $currentOrientation")
            }
            1 -> {
                rotation = 90f
                Log.d(TAG_GR, "Landscape: $currentOrientation")
            }
            2 -> {
                rotation = 180f
                Log.d(TAG_GR, "Rev_port:$currentOrientation")
            }
            3 -> {
                rotation = 270f

                Log.d(TAG_GR, "Rev_land:$currentOrientation")
            }
        }

        canvas.drawRect(collisionBox,paint) // backup in case image does not render
        //val rotatedImage = Bitmap.createBitmap(image, 0, 0, 28, 43, null, true)
        canvas.drawBitmap(image!!.rotate(rotation), null, collisionBox, null)
    }

    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
}
