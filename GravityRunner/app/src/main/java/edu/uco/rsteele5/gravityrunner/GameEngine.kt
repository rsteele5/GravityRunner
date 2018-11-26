package edu.uco.rsteele5.gravityrunner

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.*
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import edu.uco.rsteele5.gravityrunner.control.CollisionDetector
import edu.uco.rsteele5.gravityrunner.control.LevelController
import edu.uco.rsteele5.gravityrunner.control.OrientationManager
import edu.uco.rsteele5.gravityrunner.control.PlayerController
import edu.uco.rsteele5.gravityrunner.control.OrientationManager.OrientationListener
import edu.uco.rsteele5.gravityrunner.control.OrientationManager.ScreenOrientation
import edu.uco.rsteele5.gravityrunner.control.OrientationManager.ScreenOrientation.*
import edu.uco.rsteele5.gravityrunner.model.PhysicsVector
import edu.uco.rsteele5.gravityrunner.model.entity.player.PlayerAnimator

const val COSTUME = "costume"

class GameEngine : AppCompatActivity(), OrientationListener {

    var orientationManager: OrientationManager? = null
    var orientation: ScreenOrientation = PORTRAIT
    var fps: Long = 0

    var gravSpeed = 10f
    var currentLevel = 0
    val MAXLEVEL = 5
    var playerCostume: Bitmap? = null

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

        currentLevel = intent.getIntExtra(LEVEL, 1)//receive int from levelArrayAdapter
        playerCostume = intent.getParcelableExtra("costume")
        gameView = GameView(this, currentLevel)
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

    override fun onResume() {
        super.onResume()
        gameView!!.resume()
    }
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
        runOnUiThread {
            gameView!!.pause()
            val alert = AlertDialog.Builder(this@GameEngine)
            alert.setCancelable(false)
            alert.setTitle(getString(R.string.finished_menu_title))
            alert.setMessage(getString(R.string.finished_menu_message))
            alert.setPositiveButton(getString(R.string.finished_menu_btn_continue)) { _: DialogInterface?, _: Int ->
                val levelData = LevelData(currentLevel, gameView!!.getCurrentScore(), gameView!!.getCoinsCollected())
                if(currentLevel >= MAXLEVEL)
                    finish()
                else {
                    currentLevel++
                    gameView!!.resetLevel()
                    gameView!!.resume()
                }
            }
            alert.setNeutralButton(getString(R.string.finished_menu_btn_return_to_level_select)) { _: DialogInterface?, _: Int ->
                finish()
            }
            alert.show()
        }
    }

    fun showFailedMenu(){
        runOnUiThread {
            gameView!!.pause()
            val alert = AlertDialog.Builder(this@GameEngine)
            alert.setCancelable(false)
            alert.setTitle(getString(R.string.fail_menu_title))
            alert.setMessage(getString(R.string.fail_menu_message))
            alert.setPositiveButton(getString(R.string.fail_menu_btn_restart)) { _: DialogInterface?, _: Int ->
                gameView!!.resetLevel()
                gameView!!.resume()
            }
            alert.setNeutralButton(getString(R.string.fail_menu_btn_return_to_level_select)) { _: DialogInterface?, _: Int ->
                finish()
            }
            alert.show()
        }
    }

    private fun showPauseMenu(){
        runOnUiThread{
            val alert = AlertDialog.Builder(this@GameEngine)
            alert.setCancelable(false)
            alert.setTitle(getString(R.string.pause_menu_title))
            alert.setMessage(getString(R.string.pause_menu_message))
            alert.setPositiveButton(getString(R.string.pause_menu_btn_restart)){ _: DialogInterface?, _: Int ->
                gameView!!.resetLevel()
                gameView!!.resume()
            }
            alert.setNegativeButton(getString(R.string.pause_menu_btn_resume)) {_: DialogInterface?, _: Int ->
                gameView!!.resume()
            }
            alert.setNeutralButton(getString(R.string.pause_menu_btn_return_to_level_select)){ _: DialogInterface?, _: Int ->
                finish()
            }
            alert.show()
            gameView!!.pause()
        }
    }

    internal inner class GameView(context: Context, level: Int) : SurfaceView(context), Runnable {

        private var gameThread: Thread? = null
        private var ourHolder: SurfaceHolder? = null

        private var canvas: Canvas? = null
        var paint: Paint? = null

        //Controllers
        private val collisionDetector = CollisionDetector()
        private val playerController = PlayerController(
            BitmapFactory.decodeResource(resources, R.drawable.bob),
            PlayerAnimator(resources,6,6),
            (getScreenWidth() - 52).toFloat(),
            (getScreenHeight() - 100).toFloat())
        private val levelController = LevelController(resources,
            (getScreenWidth() - 52).toFloat(),
            (getScreenHeight() - 100).toFloat())

        @Volatile
        var playing: Boolean = false

        private var currentScore: Long = 0

        private var timeThisFrame: Long = 0

        init {
            ourHolder = holder
            paint = Paint()

            currentScore = levelController.loadLevel(level)
            playerController.setCostume(BitmapFactory.decodeResource(resources, R.drawable.dragon))      //TODO: Change back to playerCostume

            playing = true
            gameThread = Thread(this)

        }

        override fun run() {
            Thread.sleep(2000)
            while (playing && waitTime <= System.currentTimeMillis() + loadingTime) {

                val startFrameTime = System.currentTimeMillis()

                if(playerController.getHitPoints() <= 0){
                    showFailedMenu()
                }

                update()

                draw()

                //Calculate FPS and Score
                timeThisFrame = System.currentTimeMillis() - startFrameTime
                if (timeThisFrame > 0) {
                    fps = 1000 / timeThisFrame
                    currentScore -= fps
                }
            }
        }

        fun update() {
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
                if(collisionDetector.processPlayerEntityCollision(
                    playerController.player!!,
                    levelController.currentLevel!!.gameEntities)){
                    showFinishedMenu()
                }
                //Calculate motion vector
                else motionVector = calculateMotionVector(collisionDetector.getNormalVector())

            }
        }

        private fun calculateMotionVector(normal: PhysicsVector): PhysicsVector{
            val ngj = normal.add(gravityVector).add(playerController.getJumpingVector())
            if(ngj.magnitude == 0f) {
                playerController.startRun(orientation)
                playerController.setAnimation(0)
                playerController.incrementRun()
            }
            else {
                playerController.depricateRun()
                if(normal.x == 0f || normal.y == 0f){
                    playerController.setAnimation(1) // Jump animation
                }
            }

            return ngj.add(playerController.getRunningVector())
////////////////////////////////////Works but poorly//////////////////////////////////////////////////
//                                                                                                  //
//            val vectorGN = normal.add(gravity)                                                    //
//            val running = getRunningVector()                                                      //
//            val motion = when {                                                                   //
//                    //On ground Return running                                                    //
//                    vectorGN.magnitude == 0f -> vectorGN.add(running)                             //
//                    //In corner return Zero Vector                                                //
//                    vectorGN.add(running).magnitude == 0f -> PhysicsVector()                      //
//                    //else falling due to gravity                                                 //
//                    else -> vectorGN                                                              //
//                }                                                                                 //
//                                                                                                  //
//////////////////////////////////////////////////////////////////////////////////////////////////////
        }

        //Controls when the player can jump
        override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
            when (motionEvent.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    val normal = collisionDetector.getNormalVector()
                    if(!normal.approximateOpposite(playerController.getRunningVector())){
                        if(playerController.getJumpingVector().magnitude == 0f
                            && normal.magnitude > 0f
                            && playerController.getRunningVector().magnitude > 0f){
                            playerController.startJump(orientation)
                        }
                    }
                }
            }
            return true
        }

        fun draw() {
            if (ourHolder!!.surface.isValid) {
                canvas = ourHolder!!.lockCanvas()

                //TODO: The background will get done here
                canvas!!.drawColor(Color.argb(255, 0, 0, 0))
                //---------------------------------------
                paint!!.color = Color.argb(255, 249, 129, 0)
                if(levelController.isCurrentLevelLoaded()) {
                    levelController.draw(canvas!!, paint!!)
                    playerController.draw(canvas!!, paint!!)
                }else{
                    canvas!!.drawCircle(getScreenWidth()/2f, getScreenHeight()/2f, 20f, paint!!)
                }
                paint!!.textSize = 40f
                drawUI()
                ourHolder!!.unlockCanvasAndPost(canvas)
            }

        }

        private fun drawUI() {
            canvas!!.save()
            val xOffSet: Float
            val yOffset: Float
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

            if(playerController.player!!.jumpBoost) {
                var jumpBoostRectF = RectF(
                    canvas!!.width/2 - 170f,
                    yOffset,
                    canvas!!.width/2 - 110f,
                    yOffset + 60
                )
                canvas!!.drawBitmap(
                    BitmapFactory.decodeResource(resources, R.drawable.jump_boost_0), null,
                    jumpBoostRectF, paint
                )
            }

            canvas!!.drawText("FPS:$fps", xOffSet, yOffset, paint!!)
            canvas!!.drawText("Score:$currentScore", xOffSet, yOffset + 40f, paint!!)
            canvas!!.drawText("Hit Points:${playerController.getHitPoints()}",xOffSet, yOffset + 80f, paint!!)
            canvas!!.drawText("Coins:${playerController.getCoins()}",xOffSet, yOffset + 120f, paint!!)
            canvas!!.restore()
        }

        fun getCoinsCollected(): Int {return playerController.getCoins()}

        fun getCurrentScore(): Long {return currentScore}

        fun resetLevel(){
            levelController.loadLevel(currentLevel)
            playerController.reset()
            collisionDetector.resetNormalVector()
        }

        fun pause() {
            playing = false
            gameThread!!.join()
        }
        fun resume() {
            playing = true
            gameThread = Thread(this)
            gameThread!!.start()
        }
    }
}