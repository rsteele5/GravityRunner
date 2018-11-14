package edu.uco.rsteele5.gravityrunner.model

import android.graphics.*

class SpeedBoost(image: Bitmap, x: Float, y: Float) : GameEntity(image, x, y), PowerUp {

    init {
        width = 80f
        height = 80f
        collisionBox = RectF(xPos, yPos, width+xPos, height+yPos)
    }

    override fun applyPowerUp(player: Player) {
        player.setSpeedBoost()
    }
}