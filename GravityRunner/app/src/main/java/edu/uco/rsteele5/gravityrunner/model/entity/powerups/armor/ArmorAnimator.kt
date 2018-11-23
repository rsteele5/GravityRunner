package edu.uco.rsteele5.gravityrunner.model.entity.powerups.armor

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import edu.uco.rsteele5.gravityrunner.R
import edu.uco.rsteele5.gravityrunner.model.entity.Animator

class ArmorAnimator(resources: Resources?, framesToDisplay: Int, frameTimer: Int)
    : Animator(resources, framesToDisplay, frameTimer){

    private val armor0 = BitmapFactory.decodeResource(resources, R.drawable.armor_0)
    private val armor1 = BitmapFactory.decodeResource(resources, R.drawable.armor_1)
    private val armor2 = BitmapFactory.decodeResource(resources, R.drawable.armor_2)
    private val armor3 = BitmapFactory.decodeResource(resources, R.drawable.armor_3)
    private val armor4 = BitmapFactory.decodeResource(resources, R.drawable.armor_4)
    private val armor5 = BitmapFactory.decodeResource(resources, R.drawable.armor_5)

    init{
        initializeAnimations()
        setAnimation(0)
        currentImage = currentAnimation[0]
        imageIndex = 0
    }

    override fun initializeAnimations() {
        val armorAnimation = ArrayList<Bitmap>()

        armorAnimation.add(armor0)
        armorAnimation.add(armor1)
        armorAnimation.add(armor2)
        armorAnimation.add(armor3)
        armorAnimation.add(armor4)
        armorAnimation.add(armor5)

        animations.add(armorAnimation)
    }
}