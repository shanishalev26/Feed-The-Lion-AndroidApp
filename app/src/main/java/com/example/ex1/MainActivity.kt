package com.example.ex1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ex1.data.HighScore
import com.example.ex1.interfaces.TiltCallback
import com.example.ex1.logic.GameEngine
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.ex1.logic.GameManager
import com.example.ex1.utilities.BackgroundMusicPlayer
import com.example.ex1.utilities.HighScoreManager
import com.example.ex1.utilities.SignalManager
import com.example.ex1.utilities.SingleSoundPlayer
import com.example.ex1.utilities.TiltDetector
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private lateinit var gameManager: GameManager
    private lateinit var mainGrid: GridLayout
    private lateinit var matrix: Array<Array<Int>>
    private var isGameRunning = false
    private lateinit var lettuceOnJob: Job
    private lateinit var main_IMG_hearts: Array<AppCompatImageView>
    private lateinit var main_IMG_lettuces: Array<Array<AppCompatImageView>>
    private lateinit var main_IMG_lions: Array<AppCompatImageView>
    private lateinit var main_FAB_left: ExtendedFloatingActionButton
    private lateinit var main_FAB_right: ExtendedFloatingActionButton
    private lateinit var gameEngine: GameEngine

    private lateinit var main_LBL_score: TextView

    private lateinit var tiltDetector: TiltDetector
    private lateinit var controlMode: String

    private lateinit var main_LBL_summary: TextView

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        BackgroundMusicPlayer.init(this)
        BackgroundMusicPlayer.getInstance().setResourceId(R.raw.game_music_background)
        BackgroundMusicPlayer.getInstance().playMusic()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        gameManager = GameManager()
        gameEngine = GameEngine()

        matrix = Array(6) { Array(5) { GameEngine.EMPTY } }

        //
        controlMode = intent.getStringExtra("CONTROL_MODE") ?: "BUTTONS_SLOW"

        findViews()
        initViews()
        startLettuceLoop()
        updateScore()

        when (controlMode) {
            "BUTTONS_SLOW" -> startButtonControl(speed = 1000L)
            "BUTTONS_FAST" -> startButtonControl(speed = 500L)
            "SENSOR" -> startSensorControl()
        }

    }

    private fun findViews() {
        mainGrid = findViewById(R.id.main_grid)
        buildLettuceMatrix()
        findLionViews()
        findHeartViews()
        findButtons()
        main_LBL_score = findViewById(R.id.main_LBL_score)
        main_LBL_summary = findViewById(R.id.main_LBL_summary)

    }

    private fun findLionViews() {
        main_IMG_lions = arrayOf(
            findViewById(R.id.main_IMG_lion0),
            findViewById(R.id.main_IMG_lion1),
            findViewById(R.id.main_IMG_lion2),
            findViewById(R.id.main_IMG_lion3),
            findViewById(R.id.main_IMG_lion4)
        )
    }

    private fun findHeartViews() {
        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )
    }


    private fun findButtons() {
        main_FAB_left = findViewById(R.id.main_FAB_left)
        main_FAB_right = findViewById(R.id.main_FAB_right)
    }

    private fun buildLettuceMatrix() {
        main_IMG_lettuces = Array(6) { row ->
            Array(5) { col ->
                val imageView = AppCompatImageView(this)
                imageView.layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    rowSpec = GridLayout.spec(row, 1f)
                    columnSpec = GridLayout.spec(col, 1f)
                }
                imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                imageView.adjustViewBounds = true
                imageView.visibility = View.INVISIBLE
                mainGrid.addView(imageView)
                imageView


            }
        }
    }

    private fun initViews() {
        if (controlMode != "SENSOR") {
            main_FAB_left.setOnClickListener { onLeftClick() }
            main_FAB_right.setOnClickListener { onRightClick() }
        } else {
            main_FAB_left.visibility = View.INVISIBLE
            main_FAB_right.visibility = View.INVISIBLE
        }



        updateLionPosition()
    }

    private fun onLeftClick() {
        gameManager.moveLeft()
        updateLionPosition()
    }

    private fun onRightClick() {
        gameManager.moveRight()
        updateLionPosition()
    }

    private fun updateLionPosition() {
        for (i in main_IMG_lions.indices) {
            main_IMG_lions[i].visibility = View.INVISIBLE
        }
        val col = gameManager.lionCol
        main_IMG_lions[col].visibility = View.VISIBLE
    }

    private fun startLettuceLoop() {
        if (isGameRunning) return

        isGameRunning = true
        lettuceOnJob = lifecycleScope.launch {
            while (isGameRunning) {
                runLettuceFrame()
                delay(gameManager.getSpeed())
            }
        }
    }

    private fun runLettuceFrame() {
        gameEngine.shiftObjectsDown(matrix)
        refreshLettuceUI()
        updateLionPosition()
        checkCollision()

        gameManager.increaseDistance()
    }

    private fun refreshLettuceUI() {
        for (row in 0 until matrix.size) {
            for (col in 0 until matrix[0].size) {
                when (matrix[row][col]) {
                    GameEngine.LETTUCE -> {
                        main_IMG_lettuces[row][col].setImageResource(R.drawable.lettuce)
                        main_IMG_lettuces[row][col].visibility = View.VISIBLE
                    }

                    GameEngine.STEAK -> {
                        main_IMG_lettuces[row][col].setImageResource(R.drawable.steak)
                        main_IMG_lettuces[row][col].visibility = View.VISIBLE
                    }

                    else -> {
                        main_IMG_lettuces[row][col].setImageDrawable(null)
                        main_IMG_lettuces[row][col].visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    // @SuppressLint("SetTextI18n")
    private fun checkCollision() {
        val lionCol = gameManager.lionCol
        val lastRow = matrix.size - 1
        when (matrix[lastRow][lionCol]) {
            GameEngine.LETTUCE -> {
                gameManager.hit()
                SignalManager.getInstance().vibrate()
                SignalManager.getInstance().toast("Crashing!💥")
                val soundPlayer = SingleSoundPlayer(this)
                soundPlayer.playSound(R.raw.punch)
                main_IMG_hearts[gameManager.livesLeft].visibility = View.INVISIBLE
            }

            GameEngine.STEAK -> {
                gameManager.collectSteak()
                SignalManager.getInstance().toast("Yummy! 🥩")
                updateScore()
                val soundPlayer = SingleSoundPlayer(this)
                soundPlayer.playSound(R.raw.collectcoin)
            }
        }

        if (gameManager.isGameOver()) {
            handleGameOver();
        }
    }

    private fun handleGameOver() {
        stopGame()

        // Show the summary in the middle of the screen
        val score = gameManager.score
        val distance = gameManager.distance
        main_LBL_summary.text = "🏁 Game Over!\nScore: $score\nDistance: ${distance}m"
        main_LBL_summary.visibility = View.VISIBLE

        // Show data + move to screen 3 - scores and location
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, HighScoresActivity::class.java)
            intent.putExtra("EXTRA_SCORE", gameManager.score)
            intent.putExtra("EXTRA_DISTANCE", gameManager.distance)
            startActivity(intent)
            finish()
        }, 1500)
    }

    //@SuppressLint("SetTextI18n")
    private fun updateScore() {
        main_LBL_score.text = "Score: " + gameManager.score
    }

    private fun stopGame() {
        isGameRunning = false
        if (::lettuceOnJob.isInitialized) {
            lettuceOnJob.cancel()
        }
    }

    private fun startButtonControl(speed: Long) {
        main_FAB_left.setOnClickListener {
            gameManager.moveLeft()
            updateLionPosition()
        }

        main_FAB_right.setOnClickListener {
            gameManager.moveRight()
            updateLionPosition()
        }

        gameManager.setSpeed(speed)
    }


    private fun startSensorControl() {
        tiltDetector = TiltDetector(this, object : TiltCallback {
            override fun tiltX(x: Float) {
                val isRTL = resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
                val fixedX = if (isRTL) -x else x

                if (fixedX > 2) {
                    gameManager.moveRight()
                    updateLionPosition()
                } else if (fixedX < -2) {
                    gameManager.moveLeft()
                    updateLionPosition()
                }
            }

            override fun tiltY(y: Float) {
            }
        })

        tiltDetector.start()
    }

    override fun onResume() {
        super.onResume()
        if (::tiltDetector.isInitialized) {
            tiltDetector.start()
        }
        BackgroundMusicPlayer.getInstance().playMusic()
    }

    override fun onPause() {
        super.onPause()
        if (::tiltDetector.isInitialized) {
            tiltDetector.stop()
        }
        BackgroundMusicPlayer.getInstance().pauseMusic()
    }
}

