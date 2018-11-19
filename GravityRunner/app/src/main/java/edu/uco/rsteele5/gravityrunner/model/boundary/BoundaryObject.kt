package edu.uco.rsteele5.gravityrunner.model.boundary

import android.graphics.Bitmap
import edu.uco.rsteele5.gravityrunner.model.GameObject

abstract class BoundaryObject (image: Bitmap, x: Float, y: Float) : GameObject(image) {

    init{
        xPos = x
        yPos = y
    }
}