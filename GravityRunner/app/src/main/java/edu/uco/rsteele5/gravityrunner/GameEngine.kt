package edu.uco.rsteele5.gravityrunner

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import edu.uco.rsteele5.gravityrunner.Control.CollisionDetector
import edu.uco.rsteele5.gravityrunner.OrientationManager.OrientationListener
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation.PORTRAIT
import edu.uco.rsteele5.gravityrunner.model.BitmapBob
import edu.uco.rsteele5.gravityrunner.model.BoundaryObject
import edu.uco.rsteele5.gravityrunner.model.GameObject
import edu.uco.rsteele5.gravityrunner.model.Wall
import java.util.concurrent.CopyOnWriteArrayList

const val TAG_GR = "GR"

class GameEngine : Activity(), OrientationListener {


    var orientationManager: OrientationManager? = null
    var orientation: ScreenOrientation = PORTRAIT
    var fps: Long = 0

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
        Log.d(TAG_GR, screenOrientation.toString())
        orientation = screenOrientation

        Toast.makeText(this, screenOrientation.name, Toast.LENGTH_SHORT).show()
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

    internal inner class GameView(context: Context) : SurfaceView(context), Runnable {

        private var gameThread: Thread? = null
        private var ourHolder: SurfaceHolder? = null
        var gameObjects: CopyOnWriteArrayList<GameObject>? = null
        var boundaryObjects: CopyOnWriteArrayList<BoundaryObject>? = null

        private var canvas: Canvas? = null
        var paint: Paint? = null

        val collisionDetector = CollisionDetector()
        var bitmapBob = BitmapBob(this@GameEngine, 130f,100f)

        @Volatile
        var playing: Boolean = false


        private var timeThisFrame: Long = 0

        init {
            ourHolder = holder
            paint = Paint()

            //TODO: Maybe move this out and initialize elsewhere for brevity/cohesion
            gameObjects = CopyOnWriteArrayList()
            boundaryObjects = CopyOnWriteArrayList()
            //TODO: Replace this after sprint 1, Temp Vals for testing on sprint 1
            val rectX = 10
            val rectY = 19
            gameObjects!!.add(bitmapBob)

            boundaryObjects!!.add(Wall(this@GameEngine,0f, 0f, rectX)) //Landscape TOP
            boundaryObjects!!.add(Wall(this@GameEngine,0f, 80f, rectY, true))//Landscape LEFT
            boundaryObjects!!.add(Wall(this@GameEngine,100f*rectX, 0f, rectY, true))//Landscape RIGHT
            boundaryObjects!!.add(Wall(this@GameEngine, 100f, 80f*rectY, rectX))//Landscape BOTTOM

            for(obj in boundaryObjects!!){
                gameObjects!!.add(obj)
            }

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
            collisionDetector.processPlayerBoundaryCollision(bitmapBob, boundaryObjects!!)
            for (gameObject in gameObjects!!) {
                gameObject.update()
            }
        }

        //TODO: Here we update() game objects, called as above, gravity will probably go here it is already double buffered btw
        fun draw() {
            if (ourHolder!!.surface.isValid) {
                canvas = ourHolder!!.lockCanvas()

                //TODO: The background will get done here
                canvas!!.drawColor(Color.argb(255, 26, 128, 182))

                //TODO: Find out what this does...
                paint!!.color = Color.argb(255, 249, 129, 0)
                //TODO: Loop through array to draw
                for (gameObject in gameObjects!!) {
                    gameObject.draw(canvas!!, paint!!)
                }

                paint!!.textSize = 45f
                canvas!!.drawText("FPS:$fps", 20f, 40f, paint!!)
                canvas!!.drawText("OnGround:${bitmapBob.onGround}", 20f, 80f, paint!!)


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
                MotionEvent.ACTION_DOWN -> (gameObjects!![0] as BitmapBob).isMoving = true
                MotionEvent.ACTION_UP -> (gameObjects!![0] as BitmapBob).isMoving = false
            }
            return true
        }
    }
}