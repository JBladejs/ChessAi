package com.bladejs.chess.ai.board

import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.pieces.Piece
import com.bladejs.chess.handlers.GameHandler
import com.bladejs.chess.misc.GameState

interface BoardEvaluator {
    fun estimateScore(): Int

    fun evaluateBoard(): Int {
        val state = GameHandler.checkForMate(true)
        return if (state != GameState.ONGOING_DRAW) if(state.score > 1) Int.MAX_VALUE else Int.MIN_VALUE
        else estimateScore()
    }
}