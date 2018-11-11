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
import edu.uco.rsteele5.gravityrunner.Control.LevelController
import edu.uco.rsteele5.gravityrunner.Control.PlayerController
import edu.uco.rsteele5.gravityrunner.OrientationManager.OrientationListener
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation
import edu.uco.rsteele5.gravityrunner.OrientationManager.ScreenOrientation.*
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector
import edu.uco.rsteele5.gravityrunner.model.Wall
import java.util.concurrent.CopyOnWriteArrayList

const val TAG_GR = "GR"

class GameEngine : Activity(), OrientationListener {

    var orientationManager: OrientationManager? = null
    var orientation: ScreenOrientation = PORTRAIT
    var fps: Long = 0

    var gravSpeed = 5f

    val portraitGravityVector = PhysicsVector(0f, -1f, gravSpeed)
    val landscapeGravityVector = PhysicsVector(1f, 0f, gravSpeed)
    val reversePortraitGravityVector = PhysicsVector(0f,1f, gravSpeed)
    val reverseLandscapeGravityVector = PhysicsVector(-1f, 0f, gravSpeed)

    var motionVector = PhysicsVector(0f, 0f, 0f)

    var gravityVector = portraitGravityVector

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
        return Resources.getSystem().getDisplayMetrics().widthPixels - 52
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().getDisplayMetrics().heightPixels - 100 * 4
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    internal inner class GameView(context: Context) : SurfaceView(context), Runnable {

        private var gameThread: Thread? = null
        private var ourHolder: SurfaceHolder? = null

        private var canvas: Canvas? = null
        var paint: Paint? = null

        //Controllers
        val collisionDetector = CollisionDetector()
        val playerController = PlayerController(
            BitmapFactory.decodeResource(resources, R.drawable.bob),
            getScreenWidth().toFloat(),
            getScreenHeight().toFloat())
        val levelController = LevelController(resources,
            getScreenWidth().toFloat(),
            getScreenHeight().toFloat())

        @Volatile
        var playing: Boolean = false

        private var timeThisFrame: Long = 0

        init {
            ourHolder = holder
            paint = Paint()

            levelController.loadLevelOne()

            playing = true

        }

        override fun run() {
            while (playing) {

                val startFrameTime = System.currentTimeMillis()

                update(gravityVector)

                draw()

                timeThisFrame = System.currentTimeMillis() - startFrameTime
                if (timeThisFrame > 0) {
                    fps = 1000 / timeThisFrame
                }
            }
        }

        fun update(gravityVector: PhysicsVector) {
            //Collision Detection
            collisionDetector.resetNormalVector()
            collisionDetector.processPlayerBoundaryCollision(playerController.player!!,
                levelController.currentLevel!!.boundaryObjects)
            collisionDetector.processPlayerEntityCollision(playerController.player!!,
                levelController.currentLevel!!.gameEntitys)
            //Calculate motion vector
            motionVector = calculateMotionVector(gravityVector,collisionDetector.getNormalVector())
            //Update the player and level
            playerController.update(orientation, motionVector)
            levelController.update(orientation, motionVector)
        }

        fun draw() {
            if (ourHolder!!.surface.isValid) {
                canvas = ourHolder!!.lockCanvas()

                //TODO: The background will get done here
                canvas!!.drawColor(Color.argb(255, 26, 128, 182))
                paint!!.color = Color.argb(255, 249, 129, 0)

                playerController.draw(canvas!!,paint!!)
                levelController.draw(canvas!!,paint!!)

                paint!!.textSize = 40f
                canvas!!.drawText("FPS:$fps", 20f, 40f, paint!!)
                canvas!!.drawText("Vector x:${motionVector.x}", 20f, 80f, paint!!)
                canvas!!.drawText("Vector y:${motionVector.y}", 20f, 120f, paint!!)
                canvas!!.drawText("Vector mag:${motionVector.magnitude}", 20f, 160f, paint!!)

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

        fun calculateMotionVector(gravity: PhysicsVector,
                                  normal: PhysicsVector): PhysicsVector{
            val vectorGN = normal.add(gravity)
            val running = getRunningVector()
            val motion =
                if(vectorGN.magnitude == 0f)                    //On ground Return running
                    vectorGN.add(running)
                else if(vectorGN.add(running).magnitude == 0f)  //In corner return Zero Vector
                    PhysicsVector()
                else                                            //else falling due to gravity
                    vectorGN
            /*
            Log.d(TAG_GR, "Grav- x:${gravity.x}, y:${gravity.y}, mag:${gravity.magnitude}")
            Log.d(TAG_GR, "Norm- x:${normal.x}, y:${normal.y}, mag:${normal.magnitude}")
            Log.d(TAG_GR, "G+N- x:${vectorGN.x}, y:${vectorGN.y}, mag:${vectorGN.magnitude}")
            Log.d(TAG_GR, "Overall- x:${motion.x}, y:${motion.y}, mag:${motion.magnitude}")
            */
            return motion
        }

        private fun getRunningVector(): PhysicsVector{
            return when(orientation){
                PORTRAIT -> PhysicsVector(-1f, 0f, playerController.getSpeed())
                LANDSCAPE -> PhysicsVector(0f, -1f, playerController.getSpeed())
                REVERSED_PORTRAIT -> PhysicsVector(1f, 0f, playerController.getSpeed())
                REVERSED_LANDSCAPE -> PhysicsVector(0f, 1f, playerController.getSpeed())
            }
        }
    }
}