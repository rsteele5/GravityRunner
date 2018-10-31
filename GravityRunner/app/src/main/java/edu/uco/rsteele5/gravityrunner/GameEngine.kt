package edu.uco.rsteele5.gravityrunner

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import edu.uco.rsteele5.gravityrunner.R
import edu.uco.rsteele5.gravityrunner.OrientationManager.OrientationListener
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation.PORTRAIT
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation.REVERSED_PORTRAIT
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation.LANDSCAPE
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation.REVERSED_LANDSCAPE

const val TAG = "Engine"

class GameEngine : Activity(), OrientationListener {

    var orientationManager: OrientationManager? = null
    var deltaX = 150f
    var deltaY = 150f

    private var gameView: GameView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameView = GameView(this)
        setContentView(gameView)

        orientationManager = OrientationManager(this, SensorManager.SENSOR_DELAY_NORMAL, this)
        orientationManager!!.enable()
    }

    override fun onOrientationChange(screenOrientation: ScreenOrientation) {
        Log.d(TAG, screenOrientation.toString())
        gameView?.orientation = screenOrientation
    }

    //TODO: Need to look into this more, can we use this to pause the game?
    override fun onResume() {
        super.onResume()
        gameView!!.resume()
    }

    //TODO: Need to look into this more, can we use this to pause the game?
    override fun onPause() {
        super.onPause()
        gameView!!.pause()
    }

    internal inner class GameView (context: Context) : SurfaceView(context), Runnable {

        var orientation: ScreenOrientation = PORTRAIT
        private var gameThread: Thread? = null
        private var ourHolder: SurfaceHolder? = null

        private var canvas: Canvas? = null
        var paint: Paint? = null

        @Volatile
        var playing: Boolean = false

        var fps: Long = 0
        private var timeThisFrame: Long = 0

        //TODO: Need to make this an array of game objects
        var bitmapBob: Bitmap? = null

        //TODO: Need to fix this bob specific stuff
        var isMoving = false

        //TODO: Need to fix this bob specific stuff


        //TODO: Need to fix this bob specific stuff
        var bobXPosition = 10f
        var bobYPosition = 10f

        init {
            ourHolder = holder
            paint = Paint()

            //TODO: Maybe move this out and initialize elsewhere for brevity/cohesion
            bitmapBob = BitmapFactory.decodeResource(this.resources, R.drawable.bob)

            playing = true

        }

        override fun run() {
            while (playing) {

                val startFrameTime = System.currentTimeMillis()

                //TODO: Need to loop through the array and call update() on game objects
                update()

                //TODO: Need to loop through the array and call draw() on game objects
                draw()

                timeThisFrame = System.currentTimeMillis() - startFrameTime
                if (timeThisFrame > 0) {
                    fps = 1000 / timeThisFrame
                }
            }

        }

        //TODO: Here we update() game objects, called as above, gravity will probably go here
        fun update() {
            if (isMoving) {
                when(this.orientation){
                    PORTRAIT -> {
                        if(deltaY < 0){
                            deltaY *= -1
                        }
                        if(deltaX < 0){
                            deltaX *= -1
                        }
                    }
                    LANDSCAPE -> {
                        if(deltaY < 0){
                            deltaY *= -1
                        }
                        if(deltaX > 0){
                            deltaX *= -1
                        }
                    }
                    REVERSED_PORTRAIT -> {
                        if(deltaY > 0){
                            deltaY *= -1
                        }
                        if(deltaX > 0){
                            deltaX *= -1
                        }
                    }
                    REVERSED_LANDSCAPE -> {
                        if(deltaY > 0){
                            deltaY *= -1
                        }
                        if(deltaX < 0){
                            deltaX *= -1
                        }
                    }
                }
                bobXPosition = bobXPosition + deltaX / fps
                bobYPosition = bobYPosition + deltaY / fps
            }

        }

        //TODO: Here we update() game objects, called as above, gravity will probably go here it is already double buffered btw
        fun draw() {
            if (ourHolder!!.surface.isValid) {
                canvas = ourHolder!!.lockCanvas()

                //TODO: The backgroudn will get done here
                canvas!!.drawColor(Color.argb(255, 26, 128, 182))

                //TODO: Find out what this does...
                paint!!.color = Color.argb(255, 249, 129, 0)


                paint!!.textSize = 45f
                canvas!!.drawText("FPS:$fps", 20f, 40f, paint!!)

                //TODO: Loop through array to draw
                canvas!!.drawBitmap(bitmapBob!!, bobXPosition, bobYPosition, paint)

                //Unlock canvas and post is double buffered, kinda cool how it works, check it out
                ourHolder!!.unlockCanvasAndPost(canvas)
            }

        }

        //TODO: Need to look into this more, can we use this to pause the game?
        fun pause() {
            playing = false
            try {
                gameThread!!.join()
            } catch (e: InterruptedException) {
                Log.e("Error:", "joining thread")
            }

        }

        //TODO: Need to look into this more, can we use this to pause the game?
        fun resume() {
            playing = true
            gameThread = Thread(this)
            gameThread!!.start()
        }

        //TODO: Use this for jumps and stuff
        override fun onTouchEvent(motionEvent: MotionEvent): Boolean {

            when (motionEvent.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> isMoving = true
                MotionEvent.ACTION_UP -> isMoving = false
            }
            return true
        }
    }
}