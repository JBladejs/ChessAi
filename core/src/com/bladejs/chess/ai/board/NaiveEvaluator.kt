package com.bladejs.chess.ai.board

import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.pieces.Piece

object NaiveEvaluator: BoardEvaluator {
    override fun evaluateBoard(): Int {
        return GameBoard.pieces.getPieces(Piece.Color.WHITE).size - GameBoard.pieces.getPieces(Piece.Color.BLACK).size
    }
}