package edu.uco.rsteele5.gravityrunner.model.spikes

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import edu.uco.rsteele5.gravityrunner.R

class SpikeAnimator(resources: Resources?, rotation: Float)
{
    private val spikeUp = BitmapFactory.decodeResource(resources, R.drawable.spikes_up).rotate(rotation)
    private val spikeMid = BitmapFactory.decodeResource(resources, R.drawable.spikes_mid).rotate(rotation)
    private val spikeDown = BitmapFactory.decodeResource(resources, R.drawable.spikes_down).rotate(rotation)
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
                imageIndex = 2
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
        spikeMovingUpAnimation.add(spikeDown)
        spikeMovingUpAnimation.add(spikeMid)
        spikeMovingUpAnimation.add(spikeUp)

        val spikeMovingDownAnimation = ArrayList<Bitmap>()
        spikeMovingDownAnimation.add(spikeUp)
        spikeMovingDownAnimation.add(spikeMid)
        spikeMovingDownAnimation.add(spikeDown)

        spikeAnimations.add(spikeMovingUpAnimation)
        spikeAnimations.add(spikeMovingDownAnimation)
    }
}