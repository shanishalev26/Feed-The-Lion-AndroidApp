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
import com.example.ex1.interfaces.Callback_HighScoreItemClicked
import com.example.ex1.utilities.HighScoreManager

class HighScoreFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var highScoreItemClicked: Callback_HighScoreItemClicked? = null

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
        recyclerView = v.findViewById(R.id.highScore_LST_scores)
    }

    private fun initViews() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val scores = HighScoreManager.getHighScores(requireContext())

        val adapter = HighScoreAdapter(scores) { score ->
            val parts = score.location.split(",")
            val lat = parts.getOrNull(0)?.toDoubleOrNull() ?: 0.0
            val lon = parts.getOrNull(1)?.toDoubleOrNull() ?: 0.0
            itemClicked(lat, lon)
        }

        recyclerView.adapter = adapter
    }

    private fun itemClicked(lat: Double, lon: Double) {
        highScoreItemClicked?.highScoreItemClicked(lat, lon)
    }
}
