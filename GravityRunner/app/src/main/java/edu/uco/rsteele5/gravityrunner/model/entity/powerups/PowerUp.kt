package edu.uco.rsteele5.gravityrunner.model.entity.powerups

import edu.uco.rsteele5.gravityrunner.model.entity.player.Player

interface PowerUp {
    fun applyPowerUp(player: Player)
}