package edu.uco.rsteele5.gravityrunner.model

import edu.uco.rsteele5.gravityrunner.GameEngine

abstract class GameEntity(engine: GameEngine, x: Float, y: Float): GameObject(engine) {
    init{
        xPos = x
        yPos = y
    }
}