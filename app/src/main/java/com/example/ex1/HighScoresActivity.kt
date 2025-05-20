package com.example.ex1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ex1.interfaces.Callback_HighScoreItemClicked
import com.example.ex1.ui.HighScoreFragment
import com.example.ex1.ui.MapFragment

class HighScoresActivity : AppCompatActivity(){

    private lateinit var main_FRAME_list: FrameLayout

    private lateinit var main_FRAME_map: FrameLayout

    private lateinit var mapFragment: MapFragment
    private lateinit var highScoreFragment: HighScoreFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_high_scores)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViews()
        initViews()

        val btnBack = findViewById<Button>(R.id.highScores_BTN_back)
        btnBack.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun findViews() {
        main_FRAME_list = findViewById(R.id.main_FRAME_list)
        main_FRAME_map = findViewById(R.id.main_FRAME_map)
    }

    private fun initViews() {
        mapFragment = MapFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_FRAME_map, mapFragment)
            .commit()

        highScoreFragment = HighScoreFragment()
        highScoreFragment.highScoreItemClicked =
            object : Callback_HighScoreItemClicked {
                override fun highScoreItemClicked(lat: Double, lon: Double) {
                    mapFragment.zoom(lat, lon)
                }
            }
        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_FRAME_list, highScoreFragment)
            .commit()
    }
}