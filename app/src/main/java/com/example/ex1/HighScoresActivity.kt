package com.example.ex1

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ex1.data.HighScore
import com.example.ex1.interfaces.Callback_HighScoreItemClicked
import com.example.ex1.ui.HighScoreFragment
import com.example.ex1.ui.MapFragment
import com.example.ex1.utilities.HighScoreManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices


class HighScoresActivity : AppCompatActivity(){

    private lateinit var main_FRAME_list: FrameLayout

    private lateinit var main_FRAME_map: FrameLayout

    private lateinit var mapFragment: MapFragment
    private lateinit var highScoreFragment: HighScoreFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_high_scores)

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val score = intent.getIntExtra("EXTRA_SCORE", 0)
        val distance = intent.getIntExtra("EXTRA_DISTANCE", 0)

        if (score > 0) {
            val input = EditText(this)
            input.hint = "Enter your name"

            AlertDialog.Builder(this)
                .setTitle("New High Score!")
                .setView(input)
                .setPositiveButton("Save") { _, _ ->
                    val name = input.text.toString().ifBlank { "Unknown" }

                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        val lat = location?.latitude ?: 0.0
                        val lon = location?.longitude ?: 0.0
                        val locationStr = "$lat,$lon"

                        val highScore = HighScore(name, score, distance, locationStr)
                        HighScoreManager.saveHighScore(this, highScore)

                        findViews()
                        initViews()
                    }
                }
                .setCancelable(false)
                .show()
        } else {
            findViews()
            initViews()
        }

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