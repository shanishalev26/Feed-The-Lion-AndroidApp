package com.example.ex1.logic

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.example.ex1.R

class GameEngine(private val lettuceViews: Array<Array<AppCompatImageView>>) {

    fun shiftLettucesDown(lettuceMatrix: Array<Array<Boolean>>) {
        for (row in 3 downTo 1) {
            for (col in 0..2) {
                lettuceMatrix[row][col] = lettuceMatrix[row - 1][col]
            }
        }
        for (col in 0..2) {
            lettuceMatrix[0][col] = false
        }
        val randomCol = (0..2).random()
        lettuceMatrix[0][randomCol] = true
    }
}