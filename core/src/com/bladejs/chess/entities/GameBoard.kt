package com.bladejs.chess.entities

import com.badlogic.gdx.utils.Array

class GameBoard {
    private val board = Array(8) { Array<BoardField>(8) }

    operator fun get(i: Int) = board[i]
}