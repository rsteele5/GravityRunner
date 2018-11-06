package edu.uco.rsteele5.gravityrunner.model

import edu.uco.rsteele5.gravityrunner.GameEngine
import android.graphics.Matrix


abstract class GameEntity(engine: GameEngine, x: Float, y: Float): GameObject(engine) {
    var rotateGavMatrix = Matrix()
    init{
        xPos = x
        yPos = y
    }

    //abstract fun onCollison(bound: BoundaryObject)
}