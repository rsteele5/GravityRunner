package edu.uco.rsteele5.gravityrunner.model.entity.powerups.speedboost

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import edu.uco.rsteele5.gravityrunner.R
import edu.uco.rsteele5.gravityrunner.model.entity.Animator

class SpeedBoostAnimator(resources: Resources?, framesToDisplay: Int, frameTimer: Int)
    : Animator(resources, framesToDisplay, frameTimer){

    private val speedBoost0 = BitmapFactory.decodeResource(resources, R.drawable.speed_boost_0)
    private val speedBoost1 = BitmapFactory.decodeResource(resources, R.drawable.speed_boost_1)
    private val speedBoost2 = BitmapFactory.decodeResource(resources, R.drawable.speed_boost_2)
    private val speedBoost3 = BitmapFactory.decodeResource(resources, R.drawable.speed_boost_3)

    init {
        initializeAnimations()
        setAnimation(0)
    }

    override fun initializeAnimations() {
        currentImage = speedBoost0
        val speedBoostAnimation = ArrayList<Bitmap>()
        speedBoostAnimation.add(speedBoost0)
        speedBoostAnimation.add(speedBoost1)
        speedBoostAnimation.add(speedBoost2)
        speedBoostAnimation.add(speedBoost3)

        animations.add(speedBoostAnimation)
    }
}