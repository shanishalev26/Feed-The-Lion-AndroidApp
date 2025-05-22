package com.example.ex1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ex1.utilities.HighScoreManager

class StartActivity : AppCompatActivity() {

    private lateinit var slowBtn: Button
    private lateinit var fastBtn: Button
    private lateinit var sensorsBtn: Button
    private lateinit var start_BTN_highScore: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViews();
        initViews();
    }

    private fun findViews() {
        slowBtn = findViewById(R.id.start_BTN_slow_mode)
        fastBtn = findViewById(R.id.start_BTN_fast_mode)
        sensorsBtn = findViewById(R.id.start_BTN_sensors_mode)
        start_BTN_highScore = findViewById(R.id.start_BTN_highScores)
    }

    private fun initViews() {
        slowBtn.setOnClickListener { launchGame("BUTTONS_SLOW") }
        fastBtn.setOnClickListener { launchGame("BUTTONS_FAST") }
        sensorsBtn.setOnClickListener { launchGame("SENSOR") }

        start_BTN_highScore.setOnClickListener {
            val intent = Intent(this, HighScoresActivity::class.java)
            startActivity(intent)
        }
    }

    private fun launchGame(mode: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("CONTROL_MODE", mode)
        startActivity(intent)
    }
}