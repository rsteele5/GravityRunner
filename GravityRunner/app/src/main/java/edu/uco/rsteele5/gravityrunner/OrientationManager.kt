package edu.uco.rsteele5.gravityrunner

import android.content.Context
import android.util.Log
import android.view.OrientationEventListener

class OrientationManager(context: Context, rate: Int, listener: OrientationListener) : OrientationEventListener(context, rate) {

    var screenOrientation: ScreenOrientation? = null

    private var listener: OrientationListener? = null

    enum class ScreenOrientation {
        REVERSED_LANDSCAPE, LANDSCAPE, PORTRAIT, REVERSED_PORTRAIT
    }

    init {
        setListener(listener)
    }

    override fun onOrientationChanged(orientation: Int) {
        if (orientation == -1) {
            return
        }

        Log.d(TAG, orientation.toString())

        val newOrientation: ScreenOrientation = when (orientation) {
            in 60..140 -> ScreenOrientation.REVERSED_LANDSCAPE
            in 140..220 -> ScreenOrientation.REVERSED_PORTRAIT
            in 220..300 -> ScreenOrientation.LANDSCAPE
            else -> ScreenOrientation.PORTRAIT
        }

        if (newOrientation != screenOrientation) {
            screenOrientation = newOrientation
            if (listener != null) {
                listener!!.onOrientationChange(screenOrientation!!)
            }
        }
    }

    fun setListener(listener: OrientationListener) {
        this.listener = listener
    }

    interface OrientationListener {
        fun onOrientationChange(screenOrientation: ScreenOrientation)
    }
}