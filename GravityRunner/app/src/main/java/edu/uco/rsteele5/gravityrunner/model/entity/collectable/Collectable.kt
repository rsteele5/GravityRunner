package edu.uco.rsteele5.gravityrunner.model.entity.collectable

import edu.uco.rsteele5.gravityrunner.model.entity.Player

interface Collectable {
    fun giveTo(player: Player)
}