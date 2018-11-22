package edu.uco.rsteele5.gravityrunner.model.entity.collectable

import edu.uco.rsteele5.gravityrunner.model.entity.player.Player

interface Collectable {
    fun giveTo(player: Player)
}