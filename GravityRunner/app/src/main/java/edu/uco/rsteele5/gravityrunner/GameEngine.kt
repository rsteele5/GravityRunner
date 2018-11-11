package edu.uco.rsteele5.gravityrunner

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import edu.uco.rsteele5.gravityrunner.Control.CollisionDetector
import edu.uco.rsteele5.gravityrunner.OrientationManager.OrientationListener
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation.*
import edu.uco.rsteele5.gravityrunner.model.*
import java.util.concurrent.CopyOnWriteArrayList

const val TAG_GR = "GR"

class GameEngine : Activity(), OrientationListener {

    var orientationManager: OrientationManager? = null
    var orientation: ScreenOrientation = PORTRAIT
    var fps: Long = 0

    var speed = 2f

    val portraitGravityVector = Triple(0f, -1f, speed)
    val landscapeGravityVector = Triple(1f, 0f, speed)
    val reversePortraitGravityVector = Triple(0f,1f, speed)
    val reverseLandscapeGravityVector = Triple(-1f, 0f, speed)

    val motionVector = Triple(-1f, 0f, speed)

    var gravityVector: Triple<Float, Float, Float>? = portraitGravityVector

    private var gameView: GameView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameView = GameView(this)
        setContentView(gameView)

        orientationManager = OrientationManager(this, SensorManager.SENSOR_DELAY_NORMAL, this)
        orientationManager!!.enable()
    }

    override fun onOrientationChange(screenOrientation: ScreenOrientation) {
        orientation = screenOrientation
        gravityVector = when(orientation){
            PORTRAIT -> portraitGravityVector
            LANDSCAPE -> landscapeGravityVector
            REVERSED_PORTRAIT -> reversePortraitGravityVector
            REVERSED_LANDSCAPE -> reverseLandscapeGravityVector
        }
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

    fun getScreenWidth(): Int {
        return Resources.getSystem().getDisplayMetrics().widthPixels - 60
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().getDisplayMetrics().heightPixels - 114 * 4
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    internal inner class GameView(context: Context) : SurfaceView(context), Runnable {

        private var gameThread: Thread? = null
        private var ourHolder: SurfaceHolder? = null

        var gameObjects: CopyOnWriteArrayList<GameObject>? = null
        var boundaryObjects: CopyOnWriteArrayList<BoundaryObject>? = null

        private var canvas: Canvas? = null
        var paint: Paint? = null

        val collisionDetector = CollisionDetector()
        var bitmapBob = BitmapBob(
            BitmapFactory.decodeResource(resources, R.drawable.bob),
            (getScreenWidth() / 2).toFloat(),
            (getScreenHeight() / 2).toFloat())

        var speedBoost = SpeedBoost(
            BitmapFactory.decodeResource(resources, R.drawable.speed_boost),
            200f, 200f)

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
            gameObjects!!.add(speedBoost)

            boundaryObjects!!.add(Wall(BitmapFactory.decodeResource(resources, R.drawable.stone100x80),0f, 0f, rectX)) //Landscape TOP
            boundaryObjects!!.add(Wall(BitmapFactory.decodeResource(resources, R.drawable.stone100x80),0f, 80f, rectY, true))//Landscape LEFT
            boundaryObjects!!.add(Wall(BitmapFactory.decodeResource(resources, R.drawable.stone100x80),100f*rectX, 0f, rectY, true))//Landscape RIGHT
            boundaryObjects!!.add(Wall(BitmapFactory.decodeResource(resources, R.drawable.stone100x80), 100f, 80f*rectY, rectX))//Landscape BOTTOM

            for(obj in boundaryObjects!!){
                gameObjects!!.add(obj)
            }

            playing = true

        }

        override fun run() {
            while (playing) {

                val startFrameTime = System.currentTimeMillis()

                //TODO: Need to loop through the array and call update() on game objects
                update(gravityVector!!)

                //TODO: Need to loop through the array and call draw() on game objects
                draw()

                timeThisFrame = System.currentTimeMillis() - startFrameTime
                if (timeThisFrame > 0) {
                    fps = 1000 / timeThisFrame
                }
            }
        }

        //TODO: Here we update() game objects, called as above, gravity will probably go here
        fun update(gravityVector: Triple<Float, Float, Float>) {
            collisionDetector.processPlayerBoundaryCollision(bitmapBob, boundaryObjects!!)

            collisionDetector.getNormalVector()

            //set motion vector
            for (gameObject in gameObjects!!) {
                gameObject.update(orientation, gravityVector)
            }
        }

        //TODO: Here we update() game objects, called as above, gravity will probably go here it is already double buffered btw
        fun draw() {
            if (ourHolder!!.surface.isValid) {
                canvas = ourHolder!!.lockCanvas()

                //TODO: The background will get done here
                canvas!!.drawColor(Color.argb(255, 26, 128, 182))
                paint!!.color = Color.argb(255, 249, 129, 0)

                for (gameObject in gameObjects!!) {
                    gameObject.draw(canvas!!, paint!!)
                }
                for (gameObject in boundaryObjects!!){
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
//
//            when (motionEvent.action and MotionEvent.ACTION_MASK) {
//                MotionEvent.ACTION_DOWN -> (gameObjects!![0] as BitmapBob).isMoving = true
//                MotionEvent.ACTION_UP -> (gameObjects!![0] as BitmapBob).isMoving = false
//            }
            return true
        }
    }
}