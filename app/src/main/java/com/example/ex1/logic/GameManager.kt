package com.example.ex1.logic

class GameManager {
    private var speed: Long = 1000L
    companion object {
        private const val MAX_COLS = 5
    }

    var lionCol = 2
        private set

    var livesLeft = 3
        private set

    var score = 0

    fun moveLeft() {
        if (lionCol > 0) lionCol--
    }

    fun moveRight() {
        if (lionCol < MAX_COLS - 1) lionCol++
    }

    fun hit() {
        if (livesLeft > 0) livesLeft--
    }

    fun isGameOver(): Boolean = livesLeft == 0

    fun isCollision(matrix: Array<Array<Int>>): Boolean {
        val lastRow = matrix.size - 1
        val lionCol = lionCol

        return matrix[lastRow][lionCol] != GameEngine.EMPTY
    }

    fun collectSteak() {
        score += 10
    }

    fun setSpeed(newSpeed: Long) {
        speed = newSpeed
    }

    fun getSpeed(): Long {
        return speed
    }

    var distance: Int = 0
        private set

    fun increaseDistance() {
        distance++
    }





}