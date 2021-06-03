package com.bladejs.chess.misc

enum class GameState(val score: Int) {
    ONGOING(0),
    DRAW(0),
    WHITE_WON(1),
    BLACK_WON(-1),
}