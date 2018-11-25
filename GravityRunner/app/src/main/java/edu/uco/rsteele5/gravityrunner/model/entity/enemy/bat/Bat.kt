package edu.uco.rsteele5.gravityrunner.model.entity.enemy.bat

import android.graphics.*
import edu.uco.rsteele5.gravityrunner.control.OrientationManager
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector
import edu.uco.rsteele5.gravityrunner.model.entity.GameEntity
import edu.uco.rsteele5.gravityrunner.model.entity.enemy.Enemy


class Bat(image: Bitmap, animator: BatAnimator, x: Float, y: Float, var vertical: Boolean, var size: Int)
    : GameEntity(image, x, y), Enemy
{
    var animator: BatAnimator
    private var startPoint: Pair<Float, Float>
    private var endPoint: Pair<Float, Float>
    private var speed = 7f


    init {
        width = 80f
        height = 80f
        if(vertical){
            startPoint = Pair(x+50,y+50)
            endPoint = Pair(x+50,y+50 + size * 100)
        }else{
            startPoint = Pair(x+50,y+50)
            endPoint = Pair(x+50 + size * 100, y+50)
        }
        if(size < 0){
            speed *= -1
        }

        collisionBox = RectF(xPos, yPos, width+xPos, height+yPos)
        this.animator = animator
        animator.setAnimation(0)
    }

    override fun update(orientation: OrientationManager.ScreenOrientation, motionVector: PhysicsVector) {
        startPoint = Pair(startPoint.first + motionVector.getDeltaX(), startPoint.second + motionVector.getDeltaY())
        endPoint = Pair(endPoint.first + motionVector.getDeltaX(), endPoint.second + motionVector.getDeltaY())

        if(collisionBox.contains(endPoint.first, endPoint.second)){
            var temp = startPoint
            startPoint = endPoint
            endPoint = temp
            speed *= -1
        }
        if(vertical){
            translate(motionVector.getDeltaX(), motionVector.getDeltaY() + speed)
        } else {
            translate(motionVector.getDeltaX() + speed, motionVector.getDeltaY() )
        }

        updateCollisionBox()
        image = animator.getCurrentFrame()
        animator.update()
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        super.draw(canvas, paint)
        //canvas.drawCircle(startPoint.first, startPoint.second, 6f, paint)
        paint!!.color = Color.argb(255, 0, 0, 255)
        //canvas.drawCircle(endPoint.first, endPoint.second, 6f, paint)
        paint!!.color = Color.argb(255, 249, 129, 0)
    }
}