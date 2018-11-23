package edu.uco.rsteele5.gravityrunner.control

import android.graphics.RectF
import edu.uco.rsteele5.gravityrunner.model.*
import edu.uco.rsteele5.gravityrunner.model.boundary.BoundaryObject
import edu.uco.rsteele5.gravityrunner.model.entity.GameEntity
import edu.uco.rsteele5.gravityrunner.model.entity.Goal
import edu.uco.rsteele5.gravityrunner.model.entity.player.Player
import edu.uco.rsteele5.gravityrunner.model.entity.collectable.Collectable
import edu.uco.rsteele5.gravityrunner.model.entity.collectable.coin.Coin
import edu.uco.rsteele5.gravityrunner.model.entity.enemy.Enemy
import edu.uco.rsteele5.gravityrunner.model.entity.powerups.PowerUp
import edu.uco.rsteele5.gravityrunner.model.entity.enemy.spikes.Spikes
import java.util.concurrent.CopyOnWriteArrayList

class CollisionDetector{

    private var normalVector = PhysicsVector()
    private val collidedBoundaries = CopyOnWriteArrayList<BoundaryObject>()
    private val collidedEntities = CopyOnWriteArrayList<GameEntity>()
    private var rPCBox = RectF()

    //Boundary Collision------------------------------------------------------------------------------------------------
    fun processPlayerBoundaryCollision(player: Player, boundaries: CopyOnWriteArrayList<BoundaryObject>){

        //Reverse Phantom collision box (saves the initial location of the player)
        rPCBox = player.getCollidableBox()

        //Find all boundaries that collide with the player
        for(bound in boundaries) {
            if(RectF.intersects(player.getCollidableBox(), bound.getCollidableBox())) {
                collidedBoundaries.add(bound)
            }
        }
        //Sort the list of boundaries in descending order by size of their intersections
        collidedBoundaries.sortWith(Comparator<BoundaryObject> { b1, b2 ->
            val inter1 = RectF()
            val inter2 = RectF()
            inter1.setIntersect(player.getCollidableBox(), b1.getCollidableBox())
            inter2.setIntersect(player.getCollidableBox(), b2.getCollidableBox())

            val inter1Area = inter1.width()*inter1.height()
            val inter2Area = inter2.width()*inter2.height()

            if(inter1Area < inter2Area) 1 else -1
        })

        //Calculate a normal vector that will get the player out all the collided boxes
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

                player.getCollidableBox().set(moveCollisionBox(player.getCollidableBox(), cVector))
            }
        }
        player.getCollidableBox().set(rPCBox)   //reset the player to the center of the screen
        collidedBoundaries.clear()              //clear the now avoided boundaries from the list
    }

    private fun moveCollisionBox(collisionBox: RectF, physicsVector: PhysicsVector): RectF {
        return RectF(
            collisionBox.left - physicsVector.getDeltaX(),
            collisionBox.top - physicsVector.getDeltaY(),
            collisionBox.right - physicsVector.getDeltaX(),
            collisionBox.bottom - physicsVector.getDeltaY())
    }

    fun getNormalVector(): PhysicsVector{
        return normalVector
    }

    fun resetNormalVector(){
        normalVector.zero()
    }
    //------------------------------------------------------------------------------------------------------------------
    //TODO: Change to accept more than speed boost
    fun processPlayerEntityCollision(player: Player, entities: CopyOnWriteArrayList<GameEntity>): Boolean{
        var finished = false
        for(entity in entities){
            if(RectF.intersects(player.getCollidableBox(), entity.getCollidableBox())) {
                when(entity){
                    is PowerUp ->{
                        collidedEntities.add(entity)
                        entity.applyPowerUp(player)
                    }
                    is Collectable ->{
                        if(entity is Coin){
                            collidedEntities.add(entity)
                            entity.giveTo(player)
                        }
                    }
                    is Goal ->{
                        finished = true
                    }
                    is Enemy ->{
                        if(entity is Spikes){
                            if(RectF.intersects(player.getCollidableBox(), entity.getTriggerPulledBox())){
                                entity.setAnimation(1) // going up
                                if(RectF.intersects(player.getCollidableBox(), entity.getHitBox())){
                                    player.decrementHitPoints()
                                    if(player.getHitPoint() > 0){
                                        collidedEntities.add(entity)
                                    }
                                }
                            } else {
                                entity.setAnimation(0) // going down
                            }
                        }
                    }
                }
            }
        }

        for(entity in collidedEntities){
            entities.remove(entity)
        }

        return finished
    }

}