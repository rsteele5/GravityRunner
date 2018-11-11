package edu.uco.rsteele5.gravityrunner.model

import android.graphics.Bitmap
import android.graphics.Matrix


abstract class GameEntity(image: Bitmap, x: Float, y: Float): GameObject(image) {
    var rotateGavMatrix = Matrix()
    init{
        xPos = x
        yPos = y
    }

    //abstract fun onCollison(bound: BoundaryObject)
}