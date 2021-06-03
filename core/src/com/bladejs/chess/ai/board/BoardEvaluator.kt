package com.bladejs.chess.ai.board

import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.pieces.Piece
import com.bladejs.chess.handlers.GameHandler
import com.bladejs.chess.misc.GameState

interface BoardEvaluator {
    fun estimateScore(): Int

    fun evaluateBoard(): Int {
        val state = GameHandler.checkForMate(true)
        return when(state) {
            GameState.ONGOING -> estimateScore()
            GameState.DRAW -> 0
            GameState.WHITE_WON -> 10000000
            GameState.BLACK_WON -> -10000000
        }
    }
}