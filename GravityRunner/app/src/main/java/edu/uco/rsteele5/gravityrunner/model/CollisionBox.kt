package edu.uco.rsteele5.gravityrunner.model

import android.graphics.RectF

interface CollisionBox {
    fun getCollidableBox(): RectF
}