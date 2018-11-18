package edu.uco.rsteele5.gravityrunner.model

import android.graphics.Canvas
import android.graphics.Paint
import edu.uco.rsteele5.gravityrunner.control.OrientationManager
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector

interface Renderable {
    
    fun update(orientation: OrientationManager.ScreenOrientation, motionVector: PhysicsVector)
    fun draw(canvas: Canvas, paint: Paint)
}