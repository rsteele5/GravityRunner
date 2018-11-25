package edu.uco.rsteele5.gravityrunner.model.entity

import android.content.res.Resources
import android.graphics.Bitmap

abstract class Animator(resources: Resources?, protected var framesToDisplay: Int, protected var frameTimer: Int) {

    protected var animations: ArrayList<ArrayList<Bitmap>> = ArrayList()
    protected var currentAnimation: ArrayList<Bitmap> = ArrayList()
    protected var currentImage: Bitmap? = null
    protected var imageIndex: Int = 0

    open fun update() {
        frameTimer--
        if(frameTimer == 0) {
            imageIndex++
            if(imageIndex > currentAnimation.size - 1){
                imageIndex = 0
            }
            currentImage = currentAnimation[imageIndex]
            frameTimer = framesToDisplay
        }
    }

    fun setAnimation(index: Int) {
        if(currentAnimation != animations[index]){
            imageIndex = 0
            currentAnimation = animations[index]
        }
    }

    fun getCurrentFrame(): Bitmap {
        return currentImage!!
    }

    //call in the init of child class!!
    abstract fun initializeAnimations()
}