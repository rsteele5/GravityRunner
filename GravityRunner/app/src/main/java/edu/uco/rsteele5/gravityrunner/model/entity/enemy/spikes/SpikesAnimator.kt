package edu.uco.rsteele5.gravityrunner.model.entity.enemy.spikes

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import edu.uco.rsteele5.gravityrunner.R
import edu.uco.rsteele5.gravityrunner.model.entity.Animator

class SpikesAnimator(resources: Resources?, rotation: Float, framesToDisplay: Int, frameTimer: Int)
    : Animator(resources, framesToDisplay, frameTimer)
{
    private val spikes0 = BitmapFactory.decodeResource(resources, R.drawable.spikes_0).rotate(rotation)
    private val spikes1 = BitmapFactory.decodeResource(resources, R.drawable.spikes_1).rotate(rotation)
    private val spikes2 = BitmapFactory.decodeResource(resources, R.drawable.spikes_2).rotate(rotation)
    private val spikes3 = BitmapFactory.decodeResource(resources, R.drawable.spikes_3).rotate(rotation)

    init{
        initializeAnimations()
        currentAnimation = animations[0]
        currentImage = spikes3
        imageIndex = 3
    }

    override fun update() {
        frameTimer--
        if(frameTimer <= 0) {
            imageIndex++
            if(imageIndex > currentAnimation.size - 1){
                imageIndex = 3
            }
            currentImage = currentAnimation[imageIndex]
            frameTimer = framesToDisplay
        }
    }

    private fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    override fun initializeAnimations() {
        val spikeMovingDownAnimation = ArrayList<Bitmap>()
        spikeMovingDownAnimation.add(spikes0)
        spikeMovingDownAnimation.add(spikes1)
        spikeMovingDownAnimation.add(spikes2)
        spikeMovingDownAnimation.add(spikes3)

        val spikeMovingUpAnimation = ArrayList<Bitmap>()
        spikeMovingUpAnimation.add(spikes3)
        spikeMovingUpAnimation.add(spikes2)
        spikeMovingUpAnimation.add(spikes1)
        spikeMovingUpAnimation.add(spikes0)

        animations.add(spikeMovingDownAnimation)
        animations.add(spikeMovingUpAnimation)

    }
}