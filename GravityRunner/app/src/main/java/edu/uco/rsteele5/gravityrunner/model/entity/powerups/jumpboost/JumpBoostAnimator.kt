package edu.uco.rsteele5.gravityrunner.model.entity.powerups.jumpboost

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import edu.uco.rsteele5.gravityrunner.R
import edu.uco.rsteele5.gravityrunner.model.entity.Animator

class JumpBoostAnimator(resources: Resources?, framesToDisplay: Int, frameTimer: Int)
    : Animator(resources, framesToDisplay, frameTimer)
{
    private val jumpBoost0 = BitmapFactory.decodeResource(resources, R.drawable.jump_boost_0)
    private val jumpBoost1 = BitmapFactory.decodeResource(resources, R.drawable.jump_boost_1)
    private val jumpBoost2 = BitmapFactory.decodeResource(resources, R.drawable.jump_boost_2)
    private val jumpBoost3 = BitmapFactory.decodeResource(resources, R.drawable.jump_boost_3)
    private val jumpBoost4 = BitmapFactory.decodeResource(resources, R.drawable.jump_boost_4)
    private val jumpBoost5 = BitmapFactory.decodeResource(resources, R.drawable.jump_boost_5)
    private val jumpBoost6 = BitmapFactory.decodeResource(resources, R.drawable.jump_boost_6)
    private val jumpBoost7 = BitmapFactory.decodeResource(resources, R.drawable.jump_boost_7)

    init {
        initializeAnimations()
        setAnimation(0)
    }

    override fun initializeAnimations() {
        currentImage = jumpBoost0
        val jumpBoostAnimation = ArrayList<Bitmap>()
        jumpBoostAnimation.add(jumpBoost0)
        jumpBoostAnimation.add(jumpBoost1)
        jumpBoostAnimation.add(jumpBoost2)
        jumpBoostAnimation.add(jumpBoost3)
        jumpBoostAnimation.add(jumpBoost4)
        jumpBoostAnimation.add(jumpBoost5)
        jumpBoostAnimation.add(jumpBoost6)
        jumpBoostAnimation.add(jumpBoost7)

        animations.add(jumpBoostAnimation)
    }
}