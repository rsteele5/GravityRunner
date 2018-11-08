package edu.uco.rsteele5.gravityrunner.model

import android.graphics.Bitmap

abstract class BoundaryObject (image: Bitmap, x: Float, y: Float) : GameObject(image) {

    init{
        xPos = x
        yPos = y
    }
}