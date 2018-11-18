package edu.uco.rsteele5.gravityrunner.model.spikes

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import edu.uco.rsteele5.gravityrunner.R

class SpikesAnimator(resources: Resources?, rotation: Float)
{
    private val spikes0 = BitmapFactory.decodeResource(resources, R.drawable.spikes_0).rotate(rotation)
    private val spikes1 = BitmapFactory.decodeResource(resources, R.drawable.spikes_1).rotate(rotation)
    private val spikes2 = BitmapFactory.decodeResource(resources, R.drawable.spikes_2).rotate(rotation)
    private val spikes3 = BitmapFactory.decodeResource(resources, R.drawable.spikes_3).rotate(rotation)
    private val spikeAnimations = ArrayList<ArrayList<Bitmap>>()
    private var currentAnimation: ArrayList<Bitmap>
    private var currentImage: Bitmap
    private var framesToDisplay: Int
    private var frameTimer: Int
    private var imageIndex: Int

    init{
        initializeAnimations()
        currentAnimation = spikeAnimations[0]
        currentImage = currentAnimation[0]
        framesToDisplay = 5
        frameTimer = 4
        imageIndex = 0
    }

    fun update() {
        frameTimer--
        if(frameTimer == 0) {
            imageIndex++
            if(imageIndex > currentAnimation.size - 1){
                imageIndex = 3
            }
            currentImage = currentAnimation[imageIndex]
            frameTimer = framesToDisplay
        }
    }

    fun setAnimation(index: Int) {
        if(currentAnimation != spikeAnimations[index]){
            currentAnimation = spikeAnimations[index]
        }
    }

    fun getCurrentImage(): Bitmap{
        return currentImage
    }

    private fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    private fun initializeAnimations() {
        val spikeMovingUpAnimation = ArrayList<Bitmap>()
        spikeMovingUpAnimation.add(spikes0)
        spikeMovingUpAnimation.add(spikes1)
        spikeMovingUpAnimation.add(spikes2)
        spikeMovingUpAnimation.add(spikes3)

        val spikeMovingDownAnimation = ArrayList<Bitmap>()
        spikeMovingDownAnimation.add(spikes3)
        spikeMovingDownAnimation.add(spikes2)
        spikeMovingDownAnimation.add(spikes1)
        spikeMovingDownAnimation.add(spikes0)

        spikeAnimations.add(spikeMovingDownAnimation)
        spikeAnimations.add(spikeMovingUpAnimation)
    }
}