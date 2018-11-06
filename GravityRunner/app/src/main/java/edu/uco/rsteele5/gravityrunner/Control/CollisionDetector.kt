package edu.uco.rsteele5.gravityrunner.Control

import edu.uco.rsteele5.gravityrunner.GameEngine
import edu.uco.rsteele5.gravityrunner.model.BitmapBob
import edu.uco.rsteele5.gravityrunner.model.BoundaryObject
import edu.uco.rsteele5.gravityrunner.model.GameEntity
import edu.uco.rsteele5.gravityrunner.model.GameObject
import java.util.concurrent.CopyOnWriteArrayList

class CollisionDetector(){
    fun processPlayerBoundaryCollision(player: BitmapBob, boundarys: CopyOnWriteArrayList<BoundaryObject>){

        for(bound in boundarys) {
            if(player.getCollidableBox().intersect(bound.getCollidableBox())){
                player.onGround = true
                break
            }else{
                player.onGround = false
            }
        }
    }
}