package edu.uco.rsteele5.gravityrunner

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import edu.uco.rsteele5.gravityrunner.control.CollisionDetector
import edu.uco.rsteele5.gravityrunner.control.LevelController
import edu.uco.rsteele5.gravityrunner.control.OrientationManager
import edu.uco.rsteele5.gravityrunner.control.PlayerController
import edu.uco.rsteele5.gravityrunner.control.OrientationManager.OrientationListener
import edu.uco.rsteele5.gravityrunner.control.OrientationManager.ScreenOrientation
import edu.uco.rsteele5.gravityrunner.control.OrientationManager.ScreenOrientation.*
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector

const val TAG_GR = "GR"

class GameEngine : AppCompatActivity(), OrientationListener {

    var orientationManager: OrientationManager? = null
    var orientation: ScreenOrientation = PORTRAIT
    var fps: Long = 0

    var gravSpeed = 5f

    private val portraitGravityVector = PhysicsVector(0f, -1f, gravSpeed)
    private val landscapeGravityVector = PhysicsVector(1f, 0f, gravSpeed)
    private val reversePortraitGravityVector = PhysicsVector(0f,1f, gravSpeed)
    private val reverseLandscapeGravityVector = PhysicsVector(-1f, 0f, gravSpeed)

    var motionVector = PhysicsVector()

    var gravityVector = portraitGravityVector

    private var gameView: GameView? = null

    val loadingTime: Long = 4000
    var waitTime: Long = System.currentTimeMillis() + loadingTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setSupportActionBar(findViewById(R.id.game_engine_toolbar))

        gameView = GameView(this, 1)    //TODO: Change to getParcellable
        setContentView(gameView)

        orientationManager =
                OrientationManager(this, SensorManager.SENSOR_DELAY_NORMAL, this)
        orientationManager!!.enable()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.id_pause -> {
            showPauseMenu()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.game_engine_menu, menu)
        return super.onCreateOptionsMenu(menu)
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
        return Resources.getSystem().displayMetrics.widthPixels - 52
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels - 100
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    fun showFinishedMenu(){
        gameView!!.pause()
        val alert = AlertDialog.Builder(this@GameEngine)
        alert.setTitle(getString(R.string.finished_menu_title))
        alert.setMessage(getString(R.string.finished_menu_message))
        alert.setPositiveButton(getString(R.string.finished_menu_btn_continue)){ _: DialogInterface?, _: Int ->
            //Continue to next level
        }
        alert.setNeutralButton(getString(R.string.finished_menu_btn_return_to_level_select)){ _: DialogInterface?, _: Int ->
            finish()
        }
        alert.show()
    }

    fun showFailedMenu(){
        gameView!!.pause()
        val alert = AlertDialog.Builder(this@GameEngine)
        alert.setTitle(getString(R.string.fail_menu_title))
        alert.setMessage(getString(R.string.fail_menu_message))
        alert.setPositiveButton(getString(R.string.fail_menu_btn_restart)){ _: DialogInterface?, _: Int ->
            //Restart the level
        }
        alert.setNeutralButton(getString(R.string.fail_menu_btn_return_to_level_select)){ _: DialogInterface?, _: Int ->
            finish()
        }
        alert.show()
    }

    private fun showPauseMenu(){
        gameView!!.pause()
        val alert = AlertDialog.Builder(this@GameEngine)
        alert.setTitle(getString(R.string.pause_menu_title))
        alert.setMessage(getString(R.string.pause_menu_message))
        alert.setPositiveButton(getString(R.string.pause_menu_btn_restart)){ _: DialogInterface?, _: Int ->
            //Restart the level
        }
        alert.setNeutralButton(getString(R.string.pause_menu_btn_return_to_level_select)){ _: DialogInterface?, _: Int ->
            finish()
        }
        alert.show()
    }

    internal inner class GameView(context: Context, level: Int) : SurfaceView(context), Runnable {

        private var gameThread: Thread? = null
        private var ourHolder: SurfaceHolder? = null

        private var canvas: Canvas? = null
        var paint: Paint? = null


        //Controllers
        val collisionDetector = CollisionDetector()
        val playerController = PlayerController(
            BitmapFactory.decodeResource(resources, R.drawable.bob),
            (getScreenWidth() - 52).toFloat(),
            (getScreenHeight() - 100).toFloat())
        val levelController = LevelController(resources,
            (getScreenWidth() - 52).toFloat(),
            (getScreenHeight() - 100).toFloat())

        @Volatile
        var playing: Boolean = false

        private var timeThisFrame: Long = 0

        init {
            ourHolder = holder
            paint = Paint()

            levelController.loadLevel(level)

            playing = true
            gameThread = Thread(this)

        }

        override fun run() {
            Thread.sleep(1000)
            while (playing && waitTime <= System.currentTimeMillis() + loadingTime) {

                val startFrameTime = System.currentTimeMillis()

                update(gravityVector)

                draw()

                timeThisFrame = System.currentTimeMillis() - startFrameTime
                if (timeThisFrame > 0) {
                    fps = 1000 / timeThisFrame
                }
//                if(failed == true){
//                    showFinishedMenu()
//                } else if (finished == true){
//
//                }
            }
        }

        fun update(gravityVector: PhysicsVector) {
            if(levelController.isCurrentLevelLoaded()) {
                //Update the player and level
                playerController.update(orientation, motionVector)
                levelController.update(orientation, motionVector)
                //Collision Detection
                //--Boundaries
                collisionDetector.resetNormalVector()
                collisionDetector.processPlayerBoundaryCollision(
                    playerController.player!!,
                    levelController.currentLevel!!.boundaryObjects)
                //--Entities
                collisionDetector.processPlayerEntityCollision(
                    playerController.player!!,
                    levelController.currentLevel!!.gameEntities)
                //Calculate motion vector
                motionVector = calculateMotionVector(gravityVector, collisionDetector.getNormalVector(), motionVector)
            }
        }

        fun draw() {
            if (ourHolder!!.surface.isValid) {
                canvas = ourHolder!!.lockCanvas()

                //TODO: The background will get done here
                canvas!!.drawColor(Color.argb(255, 26, 128, 182))
                //---------------------------------------
                paint!!.color = Color.argb(255, 249, 129, 0)
                if(levelController.isCurrentLevelLoaded()) {
                    playerController.draw(canvas!!, paint!!)
                    levelController.draw(canvas!!, paint!!)
                }else{
                    canvas!!.drawCircle(getScreenWidth()/2f, getScreenHeight()/2f, 20f, paint!!)
                }
                paint!!.textSize = 40f



                drawUI()

//                canvas!!.rotate(0f, 50f, 50f)
//                canvas!!.drawText("FPS:$fps", 20f, 40f, paint!!)
//                canvas!!.drawText("Vector x:${motionVector.x}", 20f, 80f, paint!!)
//                canvas!!.drawText("Vector y:${motionVector.y}", 20f, 120f, paint!!)
//                canvas!!.drawText("Vector mag:${motionVector.magnitude}", 20f, 160f, paint!!)

                //Unlock canvas and post is double buffered, kinda cool how it works, check it out
                ourHolder!!.unlockCanvasAndPost(canvas)
            }

        }

        fun drawUI() {
            canvas!!.save()
            var xOffSet = 20f
            var yOffset = 40f
            when (orientation) {
                LANDSCAPE -> {
                    canvas!!.rotate(90f, canvas!!.width/2f, canvas!!.height/2f)
                    xOffSet = -232f
                    yOffset = 290f
                }
                REVERSED_PORTRAIT -> {
                    canvas!!.rotate(180f, canvas!!.width/2f, canvas!!.height/2f)
                    xOffSet = 20f
                    yOffset = 40f
                }
                REVERSED_LANDSCAPE -> {
                    canvas!!.rotate(270f, canvas!!.width/2f, canvas!!.height/2f)
                    xOffSet = -232f
                    yOffset = 290f
                }
                else -> {
                    xOffSet = 20f
                    yOffset = 40f
                }
            }
            if(playerController.player!!.speedBoost) {
                var speedBoostRectF = RectF(
                    canvas!!.width/2 - 120f,
                    yOffset,
                    canvas!!.width/2 - 60f,
                    yOffset + 60
                )
                canvas!!.drawBitmap(
                    BitmapFactory.decodeResource(resources, R.drawable.speed_boost), null,
                    speedBoostRectF, paint
                )
            }

            canvas!!.drawText("FPS:$fps", xOffSet, yOffset, paint!!)
            canvas!!.drawText("Vector x:${motionVector.x}", xOffSet, yOffset + 40f , paint!!)
            canvas!!.drawText("Vector y:${motionVector.y}", xOffSet, yOffset + 80f, paint!!)
            canvas!!.drawText("Vector mag:${motionVector.magnitude}", xOffSet, yOffset + 120f, paint!!)
            canvas!!.restore()
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
            waitTime = System.currentTimeMillis() + loadingTime
            playing = true
            gameThread = Thread(this)
            gameThread!!.start()
        }

        //TODO: Use this for jumps and stuff
        override fun onTouchEvent(motionEvent: MotionEvent): Boolean {

            when (motionEvent.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> playerController.startJump(orientation)
            }
            return true
        }

        private fun calculateMotionVector(gravity: PhysicsVector,
                                  normal: PhysicsVector,
                                  previous: PhysicsVector): PhysicsVector{

            var motion = when {
                normal.add(gravity).magnitude == 0f -> {getRunningVector()}
                //TODO:FIX DIS SHIT v
                normal.add(gravity).magnitude == 0f && playerController.jumping -> {getRunningVector().add(getJumpingVector())}
                //else falling due to gravity
                else -> gravity
            }

//            val vectorGN = normal.add(gravity)
//            val running = getRunningVector()
//            val motion = when {
//                    //On ground Return running
//                    vectorGN.magnitude == 0f -> vectorGN.add(running)
//                    //In corner return Zero Vector
//                    vectorGN.add(running).magnitude == 0f -> PhysicsVector()
//                    //else falling due to gravity
//                    else -> vectorGN
//                }
//            Log.d(TAG_GR, "Grav- x:${gravity.x}, y:${gravity.y}, mag:${gravity.magnitude}")
//            Log.d(TAG_GR, "Norm- x:${normal.x}, y:${normal.y}, mag:${normal.magnitude}")
//            Log.d(TAG_GR, "G+N- x:${vectorGN.x}, y:${vectorGN.y}, mag:${vectorGN.magnitude}")
//            Log.d(TAG_GR, "Overall- x:${motion.x}, y:${motion.y}, mag:${motion.magnitude}")

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

        private fun getJumpingVector(): PhysicsVector{
            return when(orientation){
                PORTRAIT -> PhysicsVector(0f, 1f, playerController.getJumpSpeed())
                LANDSCAPE -> PhysicsVector(-1f, 0f, playerController.getJumpSpeed())
                REVERSED_PORTRAIT -> PhysicsVector(0f, -1f, playerController.getJumpSpeed())
                REVERSED_LANDSCAPE -> PhysicsVector(1f, 0f, playerController.getJumpSpeed())
            }
        }
    }
}