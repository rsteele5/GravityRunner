package edu.uco.rsteele5.gravityrunner.model.entity

import android.content.res.Resources
import android.graphics.Bitmap

abstract class Animator(resources: Resources?, protected var framesToDisplay: Int, protected var frameTimer: Int) {
    protected var animation: ArrayList<Bitmap> = ArrayList()
    protected var currentImage: Bitmap? = null
    protected var imageIndex: Int = 0


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

    fun getCurrentFrame(): Bitmap {
        return currentImage!!
    }

    abstract fun initializeAnimation()
}