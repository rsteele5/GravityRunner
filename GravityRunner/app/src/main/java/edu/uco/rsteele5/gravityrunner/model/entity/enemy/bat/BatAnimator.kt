package edu.uco.rsteele5.gravityrunner.model.entity.enemy.bat

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import edu.uco.rsteele5.gravityrunner.R
import edu.uco.rsteele5.gravityrunner.model.entity.Animator

class BatAnimator(resources: Resources?, framesToDisplay: Int, frameTimer: Int)
    : Animator(resources, framesToDisplay, frameTimer) {

    private val bat0 = BitmapFactory.decodeResource(resources, R.drawable.bat_0)
    private val bat1 = BitmapFactory.decodeResource(resources, R.drawable.bat_1)
    private val bat2 = BitmapFactory.decodeResource(resources, R.drawable.bat_2)
    private val bat3 = BitmapFactory.decodeResource(resources, R.drawable.bat_3)
    private val bat4 = BitmapFactory.decodeResource(resources, R.drawable.bat_4)
    private val bat5 = BitmapFactory.decodeResource(resources, R.drawable.bat_5)

    init{
        initializeAnimations()
        setAnimation(0)
        currentImage = currentAnimation[0]
        imageIndex = 0
    }

    override fun initializeAnimations() {
        val batAnimation = ArrayList<Bitmap>()

        batAnimation.add(bat0)
        batAnimation.add(bat1)
        batAnimation.add(bat2)
        batAnimation.add(bat3)
        batAnimation.add(bat4)
        batAnimation.add(bat5)


        animations.add(batAnimation)
    }
}