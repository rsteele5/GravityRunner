package edu.uco.rsteele5.gravityrunner

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameEngine : Activity() {

    // gameView will be the view of the game
    // It will also hold the logic of the game
    // and respond to screen touches as well
    internal var gameView: GameView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize gameView and set it as the view
        gameView = GameView(this)
        setContentView(gameView)

    }

    // GameView class will go here

    // Notice we implement runnable so we have
    // A thread and can override the run method.
    internal inner class GameView// When the we initialize (call new()) on gameView
    // This special constructor method runs
        (context: Context) : SurfaceView(context), Runnable {

        // This is our thread
        var gameThread: Thread? = null

        // This is new. We need a SurfaceHolder
        // When we use Paint and Canvas in a thread
        // We will see it in action in the draw method soon.
        var ourHolder: SurfaceHolder? = null

        // A boolean which we will set and unset
        // when the game is running- or not.
        @Volatile
        var playing: Boolean = false

        // A Canvas and a Paint object
        var canvas: Canvas? = null
        var paint: Paint? = null

        // This variable tracks the game frame rate
        var fps: Long = 0

        // This is used to help calculate the fps
        private var timeThisFrame: Long = 0

        // Declare an object of type Bitmap
        var bitmapBob: Bitmap? = null

        // Bob starts off not moving
        var isMoving = false

        // He can walk at 150 pixels per second
        var walkSpeedPerSecond = 150f

        // He starts 10 pixels from the left
        var bobXPosition = 10f

        init {

            // Initialize ourHolder and paint objects
            ourHolder = holder
            paint = Paint()

            // Load Bob from his .png file
            bitmapBob = BitmapFactory.decodeResource(this.resources, R.drawable.bob)

            // Set our boolean to true - game on!
            playing = true

        }// The next line of code asks the
        // SurfaceView class to set up our object.
        // How kind.

        override fun run() {
            while (playing) {

                // Capture the current time in milliseconds in startFrameTime
                val startFrameTime = System.currentTimeMillis()

                // Update the frame
                update()

                // Draw the frame
                draw()

                // Calculate the fps this frame
                // We can then use the result to
                // time animations and more.
                timeThisFrame = System.currentTimeMillis() - startFrameTime
                if (timeThisFrame > 0) {
                    fps = 1000 / timeThisFrame
                }

            }

        }

        // Everything that needs to be updated goes in here
        // In later projects we will have dozens (arrays) of objects.
        // We will also do other things like collision detection.
        fun update() {

            // If bob is moving (the player is touching the screen)
            // then move him to the right based on his target speed and the current fps.
            if (isMoving) {
                bobXPosition += walkSpeedPerSecond / fps
            }

        }

        // Draw the newly updated scene
        fun draw() {

            // Make sure our drawing surface is valid or we crash
            if (ourHolder!!.surface.isValid) {
                // Lock the canvas ready to draw
                canvas = ourHolder!!.lockCanvas()

                // Draw the background color
                canvas!!.drawColor(Color.argb(255, 26, 128, 182))

                // Choose the brush color for drawing
                paint!!.color = Color.argb(255, 249, 129, 0)

                // Make the text a bit bigger
                paint!!.textSize = 45f

                // Display the current fps on the screen
                canvas!!.drawText("FPS:$fps", 20f, 40f, paint!!)

                // Draw bob at bobXPosition, 200 pixels
                canvas!!.drawBitmap(bitmapBob!!, bobXPosition, 200f, paint)

                // Draw everything to the screen
                ourHolder!!.unlockCanvasAndPost(canvas)
            }

        }

        // If SimpleGameEngine Activity is paused/stopped
        // shutdown our thread.
        fun pause() {
            playing = false
            try {
                gameThread!!.join()
            } catch (e: InterruptedException) {
                Log.e("Error:", "joining thread")
            }

        }

        // If SimpleGameEngine Activity is started then
        // start our thread.
        fun resume() {
            playing = true
            gameThread = Thread(this)
            gameThread!!.start()
        }

        // The SurfaceView class implements onTouchListener
        // So we can override this method and detect screen touches.
        override fun onTouchEvent(motionEvent: MotionEvent): Boolean {

            when (motionEvent.action and MotionEvent.ACTION_MASK) {

                // Player has touched the screen
                MotionEvent.ACTION_DOWN ->

                    // Set isMoving so Bob is moved in the update method
                    isMoving = true

                // Player has removed finger from screen
                MotionEvent.ACTION_UP ->

                    // Set isMoving so Bob does not move
                    isMoving = false
            }
            return true
        }

    }
    // This is the end of our GameView inner class

    // More SimpleGameEngine methods will go here

    // This method executes when the player starts the game
    override fun onResume() {
        super.onResume()

        // Tell the gameView resume method to execute
        gameView!!.resume()
    }

    // This method executes when the player quits the game
    override fun onPause() {
        super.onPause()

        // Tell the gameView pause method to execute
        gameView!!.pause()
    }

}