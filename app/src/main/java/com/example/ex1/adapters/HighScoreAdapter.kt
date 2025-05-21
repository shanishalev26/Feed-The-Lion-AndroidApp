package com.example.ex1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ex1.R
import com.example.ex1.data.HighScore

class HighScoreAdapter(
    private val scores: List<HighScore>,
    private val callback: ((HighScore) -> Unit)? = null
) : RecyclerView.Adapter<HighScoreAdapter.HighScoreViewHolder>() {

    class HighScoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeText: TextView = view.findViewById(R.id.item_LBL_place)
        val scoreText: TextView = view.findViewById(R.id.item_LBL_score)
        val locationText: TextView = view.findViewById(R.id.item_LBL_location)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighScoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_high_score, parent, false)
        return HighScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: HighScoreViewHolder, position: Int) {
        val highScore = scores[position]
        holder.placeText.text = "${position + 1}."
        holder.scoreText.text = "Score: ${highScore.score}, Distance: ${highScore.distance}"
        holder.locationText.text = highScore.location

        holder.itemView.setOnClickListener {
            callback?.invoke(highScore)
        }
    }

    override fun getItemCount(): Int = scores.size
}
