package edu.uco.rsteele5.gravityrunner.model.entity.collectable.coin

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import edu.uco.rsteele5.gravityrunner.R

class CoinAnimator(resources: Resources?)
{
    private val coin0 = BitmapFactory.decodeResource(resources, R.drawable.coin_0)
    private val coin1 = BitmapFactory.decodeResource(resources, R.drawable.coin_1)
    private val coin2 = BitmapFactory.decodeResource(resources, R.drawable.coin_2)
    private val coin3 = BitmapFactory.decodeResource(resources, R.drawable.coin_3)
    private val coin4 = BitmapFactory.decodeResource(resources, R.drawable.coin_4)
    private val coin5 = BitmapFactory.decodeResource(resources, R.drawable.coin_5)
    private val coin6 = BitmapFactory.decodeResource(resources, R.drawable.coin_6)
    private val coin7 = BitmapFactory.decodeResource(resources, R.drawable.coin_7)
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
        animation.add(coin0)
        animation.add(coin1)
        animation.add(coin2)
        animation.add(coin3)
        animation.add(coin4)
        animation.add(coin5)
        animation.add(coin6)
        animation.add(coin7)
    }
}