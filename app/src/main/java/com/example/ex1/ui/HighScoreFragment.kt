package com.example.ex1.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ex1.R
import com.example.ex1.adapters.HighScoreAdapter
import com.example.ex1.data.HighScore
import com.example.ex1.interfaces.Callback_HighScoreItemClicked
import com.example.ex1.utilities.HighScoreManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class HighScoreFragment : Fragment() {

    private lateinit var highScore_ET_text: TextInputEditText
    private lateinit var highscore_BTN_send: MaterialButton
    private lateinit var recyclerView: RecyclerView

    var highScoreItemClicked: Callback_HighScoreItemClicked? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_high_score, container, false)
        findViews(v)
        initViews()
        return v
    }

    private fun findViews(v: View) {
        highScore_ET_text = v.findViewById(R.id.highScore_ET_text)
        highscore_BTN_send = v.findViewById(R.id.highScore_BTN_send)
        recyclerView = v.findViewById(R.id.highScore_LST_scores)
    }

    private fun initViews() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = HighScoreAdapter(
            HighScoreManager.getHighScores(requireContext())
        ) { score ->
            val parts = score.location.split(",")
            val lat = parts.getOrNull(0)?.toDoubleOrNull() ?: 0.0
            val lon = parts.getOrNull(1)?.toDoubleOrNull() ?: 0.0
            itemClicked(lat, lon)
        }

        val allScores = HighScoreManager.getHighScores(requireContext())
        Log.d("DEBUG_LIST_SIZE", "Number of scores: ${allScores.size}")

        recyclerView.adapter = adapter

        highscore_BTN_send.setOnClickListener {
            val coordinates = highScore_ET_text.text?.split(",")
            val lat = coordinates?.getOrNull(0)?.toDoubleOrNull() ?: 0.0
            val lon = coordinates?.getOrNull(1)?.toDoubleOrNull() ?: 0.0

            val score = requireActivity().intent.getIntExtra("EXTRA_SCORE", 0)
            val distance = requireActivity().intent.getIntExtra("EXTRA_DISTANCE", 0)
            val location = "$lat,$lon"

            HighScoreManager.saveHighScore(requireContext(), HighScore(score, distance, location))

            itemClicked(lat, lon)

            val updatedScores = HighScoreManager.getHighScores(requireContext())
            recyclerView.adapter = HighScoreAdapter(updatedScores) { score ->
                val parts = score.location.split(",")
                val lat = parts.getOrNull(0)?.toDoubleOrNull() ?: 0.0
                val lon = parts.getOrNull(1)?.toDoubleOrNull() ?: 0.0
                itemClicked(lat, lon)
            }
        }
    }

    private fun itemClicked(lat: Double, lon: Double) {
        highScoreItemClicked?.highScoreItemClicked(lat, lon)
    }
}
