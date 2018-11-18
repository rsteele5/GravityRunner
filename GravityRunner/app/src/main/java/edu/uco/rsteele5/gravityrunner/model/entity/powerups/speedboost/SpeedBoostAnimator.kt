package edu.uco.rsteele5.gravityrunner.model.entity.powerups.speedboost

import android.content.res.Resources
import android.graphics.BitmapFactory
import edu.uco.rsteele5.gravityrunner.R
import edu.uco.rsteele5.gravityrunner.model.entity.Animator

class SpeedBoostAnimator(resources: Resources): Animator(resources){

    private val speedBoost0 = BitmapFactory.decodeResource(resources, R.drawable.speed_boost_0)
    private val speedBoost1 = BitmapFactory.decodeResource(resources, R.drawable.speed_boost_1)
    private val speedBoost2 = BitmapFactory.decodeResource(resources, R.drawable.speed_boost_2)
    private val speedBoost3 = BitmapFactory.decodeResource(resources, R.drawable.speed_boost_3)

    init {
        initializeAnimation()
        currentImage = animation[0]
    }


    override fun initializeAnimation() {
        animation.add(speedBoost0)
        animation.add(speedBoost1)
        animation.add(speedBoost2)
        animation.add(speedBoost3)
    }
}