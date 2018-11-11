package edu.uco.rsteele5.gravityrunner.Control

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import edu.uco.rsteele5.gravityrunner.OrientationManager
import edu.uco.rsteele5.gravityrunner.model.*
import java.util.concurrent.CopyOnWriteArrayList

const val TAG_CD = "CD" //TODO: Remove

class CollisionDetector{
    private var normalVector = PhysicsVector()
    private val collidedBoundaries = CopyOnWriteArrayList<BoundaryObject>()

    //TODO: REMOVE Canvas and paint
    fun processPlayerBoundaryCollision(player: Player, boundaries: CopyOnWriteArrayList<BoundaryObject>){

        for(bound in boundaries) {
            if(RectF.intersects(player.getCollidableBox(), bound.getCollidableBox())) {
                collidedBoundaries.add(bound)
            }
        }
        Log.d(TAG_CD, collidedBoundaries.size.toString())
        Log.d(TAG_CD, collidedBoundaries.toString())
        collidedBoundaries.sortWith(Comparator<BoundaryObject> { b1, b2 ->
            val inter1 = RectF()
            val inter2 = RectF()
            inter1.setIntersect(player.getCollidableBox(), b1.getCollidableBox())
            inter2.setIntersect(player.getCollidableBox(), b2.getCollidableBox())

            val inter1Area = inter1.width()*inter1.height()
            val inter2Area = inter2.width()*inter2.height()

            if(inter1Area >= inter2Area) 1 else -1
        })
        Log.d(TAG_CD, collidedBoundaries.toString())
        for(bound in collidedBoundaries) {
            if (RectF.intersects(player.getCollidableBox(), bound.getCollidableBox())) {
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

                val cVector = PhysicsVector(xComp, yComp, magnitude)
                normalVector = normalVector.add(cVector)

                moveCollisionBox(player.getCollidableBox(), cVector)

                Log.d(TAG_CD, "CVector- x:${cVector.x}, y:${cVector.y}, mag:${cVector.magnitude}")
                Log.d(TAG_CD, "Norm- x:${normalVector.x}, y:${normalVector.y}, mag:${normalVector.magnitude}")
            }
            Log.d(TAG_CD, "")
        }
        collidedBoundaries.clear()
        Log.d(TAG_CD, "--------------------------------")
    }

    fun getNormalVector(): PhysicsVector{
        return normalVector
    }

    fun resetNormalVector(){
        normalVector.zero()
    }

    private fun moveCollisionBox(collisionBox: RectF, physicsVector: PhysicsVector) {
        collisionBox.set(RectF(
            collisionBox.left - physicsVector.getDeltaX(),
            collisionBox.top - physicsVector.getDeltaY(),
            collisionBox.right - physicsVector.getDeltaX(),
            collisionBox.bottom - physicsVector.getDeltaY()))
    }
}