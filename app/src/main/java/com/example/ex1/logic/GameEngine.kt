package com.example.ex1.logic

class GameEngine {
    companion object {
        const val EMPTY = 0
        const val LETTUCE = 1
        const val STEAK = 2
    }

    fun shiftObjectsDown(matrix: Array<Array<Int>>) {
        for (row in matrix.size - 1 downTo 1) {
            for (col in matrix[0].indices) {
                matrix[row][col] = matrix[row - 1][col]
            }
        }

        for (col in matrix[0].indices) {
            matrix[0][col] = EMPTY
        }

        val randomCol = (0 until matrix[0].size).random()
        val objectType = if ((0..3).random() < 3) LETTUCE else STEAK
        matrix[0][randomCol] = objectType
    }
}