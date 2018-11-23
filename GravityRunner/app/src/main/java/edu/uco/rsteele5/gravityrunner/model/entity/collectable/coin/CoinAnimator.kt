package edu.uco.rsteele5.gravityrunner.model.entity.collectable.coin

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import edu.uco.rsteele5.gravityrunner.R
import edu.uco.rsteele5.gravityrunner.model.entity.Animator

class CoinAnimator(resources: Resources?, framesToDisplay: Int, frameTimer: Int)
    : Animator(resources, framesToDisplay, frameTimer)
{
    private val coin0 = BitmapFactory.decodeResource(resources, R.drawable.coin_0)
    private val coin1 = BitmapFactory.decodeResource(resources, R.drawable.coin_1)
    private val coin2 = BitmapFactory.decodeResource(resources, R.drawable.coin_2)
    private val coin3 = BitmapFactory.decodeResource(resources, R.drawable.coin_3)
    private val coin4 = BitmapFactory.decodeResource(resources, R.drawable.coin_4)
    private val coin5 = BitmapFactory.decodeResource(resources, R.drawable.coin_5)
    private val coin6 = BitmapFactory.decodeResource(resources, R.drawable.coin_6)
    private val coin7 = BitmapFactory.decodeResource(resources, R.drawable.coin_7)

    init{
        initializeAnimations()
        setAnimation(0)
        currentImage = currentAnimation[0]
        imageIndex = 0
    }

    override fun initializeAnimations() {
        val coinAnimation = ArrayList<Bitmap>()

        coinAnimation.add(coin0)
        coinAnimation.add(coin1)
        coinAnimation.add(coin2)
        coinAnimation.add(coin3)
        coinAnimation.add(coin4)
        coinAnimation.add(coin5)
        coinAnimation.add(coin6)
        coinAnimation.add(coin7)

        animations.add(coinAnimation)
    }
}