package edu.uco.rsteele5.gravityrunner.model.powerups.speedboost

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import edu.uco.rsteele5.gravityrunner.R

class SpeedBoostAnimator(resources: Resources?){

    private val speedBoost0 = BitmapFactory.decodeResource(resources, R.drawable.speed_boost_0)
    private val speedBoost1 = BitmapFactory.decodeResource(resources, R.drawable.speed_boost_1)
    private val speedBoost2 = BitmapFactory.decodeResource(resources, R.drawable.speed_boost_2)
    private val speedBoost3 = BitmapFactory.decodeResource(resources, R.drawable.speed_boost_3)
    private var animation: ArrayList<Bitmap> = ArrayList()
    private var currentImage: Bitmap
    private var framesToDisplay: Int
    private var frameTimer: Int
    private var imageIndex: Int

    init{
        initializeAnimation()
        currentImage = animation[0]
        framesToDisplay = 6
        frameTimer = 4
        imageIndex = 0
    }

    fun update() {
        frameTimer--
        if(frameTimer == 0) {
            imageIndex++
            if(imageIndex > animation.size - 1){
                imageIndex = 0
            }
            currentImage = animation[imageIndex]
            frameTimer = framesToDisplay
        }
    }

    fun getCurrentImage(): Bitmap {
        return currentImage
    }

    private fun initializeAnimation() {
        animation.add(speedBoost0)
        animation.add(speedBoost1)
        animation.add(speedBoost2)
        animation.add(speedBoost3)
    }
}