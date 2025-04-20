package com.example.ex1

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ex1.logic.GameEngine
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.ex1.logic.GameManager
import com.example.ex1.utilities.Constants
import com.example.ex1.utilities.buildLettuceMatrix


class MainActivity : AppCompatActivity() {

    private lateinit var gameManager: GameManager
    private lateinit var lettuceMatrix: Array<Array<Boolean>>

    private var isGameRunning = false
    private lateinit var lettuceOnJob: Job

    private lateinit var main_IMG_hearts: Array<AppCompatImageView>

    private lateinit var main_IMG_lettuces: Array<Array<AppCompatImageView>>

    private lateinit var main_IMG_lions: Array<AppCompatImageView>

    private lateinit var main_FAB_left: ExtendedFloatingActionButton

    private lateinit var main_FAB_right: ExtendedFloatingActionButton

    private lateinit var gameEngine: GameEngine

    private lateinit var main_LBL_gameOver: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViews()
        gameManager = GameManager()
        gameEngine = GameEngine(main_IMG_lettuces)
        initViews()
        lettuceMatrix = Array(4) { Array(3) { false } }

        startLettuceLoop()
    }

    private fun findViews(){
        main_IMG_lions = arrayOf(
            findViewById(R.id.main_IMG_lion0),
            findViewById(R.id.main_IMG_lion1),
            findViewById(R.id.main_IMG_lion2)
        )

        main_FAB_left = findViewById(R.id.main_FAB_left)
        main_FAB_right = findViewById(R.id.main_FAB_right)

        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )

        main_IMG_lettuces = buildLettuceMatrix(this)
        main_LBL_gameOver = findViewById(R.id.main_LBL_gameOver)
    }

    private fun initViews(){
        main_FAB_left.setOnClickListener {v:View ->onLeftClick() }
        main_FAB_right.setOnClickListener {v:View ->onRightClick() }
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
        for (i in 0..2) {
            main_IMG_lions[i].visibility = View.INVISIBLE
        }
        val col = gameManager.lionCol
        main_IMG_lions[col].visibility = View.VISIBLE
    }

    private fun checkCollision() {
        if (gameManager.isCollision(lettuceMatrix)) {
            gameManager.hit()
            Toast.makeText(this, "VIBRATE!", Toast.LENGTH_SHORT).show()

            main_IMG_hearts[gameManager.livesLeft].visibility = View.INVISIBLE

            if (gameManager.isGameOver()) {
                main_LBL_gameOver.visibility = View.VISIBLE
                stopGame()
            }
        }
    }

    private fun stopGame() {
        isGameRunning = false
        if (::lettuceOnJob.isInitialized) {
            lettuceOnJob.cancel()
        }
    }

    private fun startLettuceLoop() {
        if (isGameRunning) return

        isGameRunning = true
        lettuceOnJob = lifecycleScope.launch {
            while (isGameRunning) {
                runLettuceFrame()
                delay(Constants.Timer.DELAY)
            }
        }
    }
    private fun runLettuceFrame() {
        gameManager.shiftLettucesDown(lettuceMatrix)
        refreshLettuceUI()
        updateLionPosition()
        checkCollision()
    }

    private fun refreshLettuceUI() {
        for (row in 0..3) {
            for (col in 0..2) {
                if (lettuceMatrix[row][col]) {
                    main_IMG_lettuces[row][col].setImageResource(R.drawable.lettuce)
                    main_IMG_lettuces[row][col].visibility = View.VISIBLE
                } else {
                    main_IMG_lettuces[row][col].setImageDrawable(null)
                    main_IMG_lettuces[row][col].visibility = View.INVISIBLE
                }
            }
        }
    }
}