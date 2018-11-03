package edu.uco.rsteele5.gravityrunner

import android.graphics.BitmapFactory
import android.util.Log


class BitmapBob(engine: GameEngine) : GameObject(engine) {

    var isMoving = false

    init {
        isMoving = false
        image = BitmapFactory.decodeResource(engine.resources, R.drawable.bob)
        xPos = 10f
        yPos = 10f

        deltaX = 1f
        deltaY = 3f
    }

    override fun update() {
        Log.d(TAG, isMoving.toString())
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

        Log.d(TAG, "Ori: ${engine!!.orientation.name} deltaX: $deltaX")
        Log.d(TAG, "Ori: ${engine!!.orientation.name} deltaY: $deltaY")

    }

    override fun draw() {

    }
}
