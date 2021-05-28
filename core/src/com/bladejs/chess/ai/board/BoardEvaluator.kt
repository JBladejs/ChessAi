package com.bladejs.chess.ai.board

import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.pieces.Piece

interface BoardEvaluator {
    fun evaluateBoard(): Int
}