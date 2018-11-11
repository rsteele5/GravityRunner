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
    private var pCBox = RectF()
    private var pCCenter = Pair(0f,0f)
    private var currentOrientation = OrientationManager.ScreenOrientation.PORTRAIT

    //TODO: REMOVE Canvas and paint
    fun processPlayerBoundaryCollision(player: Player, boundaries: CopyOnWriteArrayList<BoundaryObject>){
        pCBox = player.getCollidableBox()
        pCCenter = player.getCenter()
        currentOrientation = player.currentOrientation

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
            inter1.setIntersect(pCBox, b1.getCollidableBox())
            inter2.setIntersect(pCBox, b2.getCollidableBox())

            val inter1Area = inter1.width()*inter1.height()
            val inter2Area = inter2.width()*inter2.height()

            if(inter1Area >= inter2Area) 1 else -1
        })
        Log.d(TAG_CD, collidedBoundaries.toString())
        for(bound in collidedBoundaries) {
            if (RectF.intersects(pCBox, bound.getCollidableBox())) {
                var intersection = RectF()
                intersection.setIntersect(pCBox, bound.getCollidableBox())

                var magnitude = if (intersection.height() > intersection.width()) {
                    intersection.width()
                } else {
                    intersection.height()
                }

                val angleDeg = Math.atan2(
                    intersection.centerY() - pCCenter.second.toDouble(),
                    intersection.centerX() - pCCenter.first.toDouble()
                )


                var xComp = Math.round(Math.cos(angleDeg)).toFloat()
                var yComp = Math.round(Math.sin(angleDeg)).toFloat()

                if (xComp.isNaN()) xComp = 0.0f
                if (yComp.isNaN()) yComp = 0.0f

                val cVector = PhysicsVector(xComp, yComp, magnitude)
                normalVector = normalVector.add(cVector)

                pCCenter = Pair(pCCenter.first + cVector.getDeltaX(),
                                pCCenter.second + cVector.getDeltaY())

                pCBox.set(RectF(
                    pCBox.left + cVector.getDeltaX(),
                    pCBox.top + cVector.getDeltaY(),
                    pCBox.right + cVector.getDeltaX(),
                    pCBox.bottom + cVector.getDeltaY()))

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
}