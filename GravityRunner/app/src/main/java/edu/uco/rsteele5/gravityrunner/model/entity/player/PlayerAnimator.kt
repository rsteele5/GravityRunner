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
    private val jumping0 = BitmapFactory.decodeResource(resources, R.drawable.player_jumping_0)
    private val jumping1 = BitmapFactory.decodeResource(resources, R.drawable.player_jumping_1)
    private val jumping2 = BitmapFactory.decodeResource(resources, R.drawable.player_jumping_2)


    init{
        initializeAnimations()
        setAnimation(0)
    }

    override fun initializeAnimations() {
        currentImage = running0
        val playerRunningAnimation = ArrayList<Bitmap>()
        playerRunningAnimation.add(running0)
        playerRunningAnimation.add(running1)
        playerRunningAnimation.add(running2)
        playerRunningAnimation.add(running3)
        playerRunningAnimation.add(running4)
        playerRunningAnimation.add(running5)
        playerRunningAnimation.add(running6)
        playerRunningAnimation.add(running7)

        val playerJumpingAnimation = ArrayList<Bitmap>()
        playerJumpingAnimation.add(jumping0)
        playerJumpingAnimation.add(jumping1)
        playerJumpingAnimation.add(jumping2)

        animations.add(playerRunningAnimation)
        animations.add(playerJumpingAnimation)
    }

    private fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
}