package edu.uco.rsteele5.gravityrunner.Control

import android.graphics.RectF
import edu.uco.rsteele5.gravityrunner.model.BitmapBob
import edu.uco.rsteele5.gravityrunner.model.BoundaryObject
import java.util.concurrent.CopyOnWriteArrayList

class CollisionDetector{
    private var normalVector: Triple<Float, Float, Float> = Triple(0f,0f,0f)

    fun processPlayerBoundaryCollision(player: BitmapBob, boundaries: CopyOnWriteArrayList<BoundaryObject>){

        for(bound in boundaries) {
            if(RectF.intersects(player.getCollidableBox(), bound.getCollidableBox())) {
                var intersection = RectF()
                intersection.setIntersect(player.getCollidableBox(), bound.getCollidableBox())

                var magnitude = if (intersection.height() > intersection.width()) {
                    intersection.width()
                } else {
                    intersection.height()
                }

                val angleDeg = Math.atan2(
                    intersection.centerY() - player.getCenter().second.toDouble(),
                    intersection.centerX() - player.getCenter().first.toDouble()
                )


                var xComp = Math.round(Math.cos(angleDeg)).toFloat()
                var yComp = Math.round(Math.sin(angleDeg)).toFloat()

                if (xComp.isNaN()) xComp = 0.0f
                if (yComp.isNaN()) yComp = 0.0f

                normalVector = Triple(xComp, yComp, -magnitude)

                //TODO: Remove this once testing is done...
                //player.debugMove(xComp * -magnitude, yComp * -magnitude)
            }
        }
    }

    fun getNormalVector(): Triple<Float, Float, Float> {
        return normalVector
    }

    fun getSpeedModifier(): Float {
        return 0f
    }
}