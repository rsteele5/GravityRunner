package edu.uco.rsteele5.gravityrunner.model.entity.player

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import edu.uco.rsteele5.gravityrunner.R
import edu.uco.rsteele5.gravityrunner.model.entity.Animator

class PlayerAnimator(resources: Resources?, framesToDisplay: Int, frameTimer: Int)
    : Animator(resources, framesToDisplay, frameTimer)
{
    private val running0 = BitmapFactory.decodeResource(resources, R.drawable.player_running_0)
    private val running1 = BitmapFactory.decodeResource(resources, R.drawable.player_running_1)
    private val running2 = BitmapFactory.decodeResource(resources, R.drawable.player_running_2)
    private val running3 = BitmapFactory.decodeResource(resources, R.drawable.player_running_3)
    private val running4 = BitmapFactory.decodeResource(resources, R.drawable.player_running_0)
    private val running5 = BitmapFactory.decodeResource(resources, R.drawable.player_running_1)
    private val running6 = BitmapFactory.decodeResource(resources, R.drawable.player_running_2)
    private val running7 = BitmapFactory.decodeResource(resources, R.drawable.player_running_3)
    private val playerAnimations = ArrayList<ArrayList<Bitmap>>()
    //private var currentAnimation: ArrayList<Bitmap>

    init{
        initializeAnimation()
        //currentAnimation = animation[0]
        //currentImage = currentAnimation[0]
        imageIndex = 0
    }

//    fun setAnimation(index: Int) {
//        if(currentAnimation != playerAnimations[index]){
//            currentAnimation = playerAnimations[index]
//        }
//    }

    override fun initializeAnimation() {
        currentImage = running0
        val playerRunningAnimation = ArrayList<Bitmap>()
        animation.add(running0)
        animation.add(running1)
        animation.add(running2)
        animation.add(running3)
        animation.add(running4)
        animation.add(running5)
        animation.add(running6)
        animation.add(running7)

        //animation.add(playerRunningAnimation)
    }

    private fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
}