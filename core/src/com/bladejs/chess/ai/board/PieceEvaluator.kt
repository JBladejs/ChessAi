package com.bladejs.chess.ai.board

import com.badlogic.gdx.utils.Array
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.pieces.*

object PieceEvaluator : BoardEvaluator{
    override fun estimateScore(): Int {
        var score = 0
        val pieces = Array<Piece>()
        pieces.addAll(GameBoard.pieces.getPieces(Piece.Color.WHITE))
        pieces.addAll(GameBoard.pieces.getPieces(Piece.Color.BLACK))
        pieces.forEach {
            val value = when(it) {
                is Pawn -> 10
                is Bishop -> 30
                is Knight -> 30
                is Rook -> 50
                is Queen -> 90
                else -> 900
            }
            score += if (it.color == Piece.Color.WHITE) value else -value
        }
        return score
    }
}