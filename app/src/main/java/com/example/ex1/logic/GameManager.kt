package com.example.ex1.logic

class GameManager {
    var lionCol = 1
        private set

    var livesLeft = 3
        private set

    fun moveLeft() {
        if (lionCol > 0) lionCol--
    }

    fun moveRight() {
        if (lionCol < 2) lionCol++
    }

    fun hit() {
        if (livesLeft > 0) livesLeft--
    }

    fun isGameOver(): Boolean = livesLeft == 0

    fun isCollision(lettuce: Array<Array<Boolean>>): Boolean {
        return lettuce[3][lionCol]
    }


}