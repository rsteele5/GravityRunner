package edu.uco.rsteele5.gravityrunner.model.entity.powerups.armor

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import edu.uco.rsteele5.gravityrunner.R

class ArmorAnimator(resources: Resources?)
{
    private val armor0 = BitmapFactory.decodeResource(resources, R.drawable.armor_0)
    private val armor1 = BitmapFactory.decodeResource(resources, R.drawable.armor_1)
    private val armor2 = BitmapFactory.decodeResource(resources, R.drawable.armor_2)
    private val armor3 = BitmapFactory.decodeResource(resources, R.drawable.armor_3)
    private val armor4 = BitmapFactory.decodeResource(resources, R.drawable.armor_4)
    private val armor5 = BitmapFactory.decodeResource(resources, R.drawable.armor_5)
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
        animation.add(armor0)
        animation.add(armor1)
        animation.add(armor2)
        animation.add(armor3)
        animation.add(armor4)
        animation.add(armor5)
    }
}