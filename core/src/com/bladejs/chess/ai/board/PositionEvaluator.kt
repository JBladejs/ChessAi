package com.bladejs.chess.ai.board

import com.badlogic.gdx.Game
import com.badlogic.gdx.utils.Array
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.pieces.*
import kotlin.math.abs

object PositionEvaluator: BoardEvaluator {
    private var endGame = false
    private val pawn = Array<Int>()
    private val bishop = Array<Int>()
    private val knight = Array<Int>()
    private val rook = Array<Int>()
    private val queen = Array<Int>()
    private val king = Array<Int>()
    private val endGameKing = Array<Int>()

    init {
        pawn.addAll(
            0,  0,  0,  0,  0,  0,  0,  0,
            50, 50, 50, 50, 50, 50, 50, 50,
            10, 10, 20, 30, 30, 20, 10, 10,
            5,  5, 10, 25, 25, 10,  5,  5,
            0,  0,  0, 20, 20,  0,  0,  0,
            5, -5,-10,  0,  0,-10, -5,  5,
            5, 10, 10,-20,-20, 10, 10,  5,
            0,  0,  0,  0,  0,  0,  0,  0
        )
        bishop.addAll(
            -20,-10,-10,-10,-10,-10,-10,-20,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5, 10, 10,  5,  0,-10,
            -10,  5,  5, 10, 10,  5,  5,-10,
            -10,  0, 10, 10, 10, 10,  0,-10,
            -10, 10, 10, 10, 10, 10, 10,-10,
            -10,  5,  0,  0,  0,  0,  5,-10,
            -20,-10,-10,-10,-10,-10,-10,-20
        )
        knight.addAll(
            -50,-40,-30,-30,-30,-30,-40,-50,
            -40,-20,  0,  0,  0,  0,-20,-40,
            -30,  0, 10, 15, 15, 10,  0,-30,
            -30,  5, 15, 20, 20, 15,  5,-30,
            -30,  0, 15, 20, 20, 15,  0,-30,
            -30,  5, 10, 15, 15, 10,  5,-30,
            -40,-20,  0,  5,  5,  0,-20,-40,
            -50,-40,-30,-30,-30,-30,-40,-50
        )
        rook.addAll(
            0,  0,  0,  0,  0,  0,  0,  0,
            5, 10, 10, 10, 10, 10, 10,  5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            0,  0,  0,  5,  5,  0,  0,  0
        )
        queen.addAll(
            -20,-10,-10, -5, -5,-10,-10,-20,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5,  5,  5,  5,  0,-10,
            -5,  0,  5,  5,  5,  5,  0, -5,
            0,  0,  5,  5,  5,  5,  0, -5,
            -10,  5,  5,  5,  5,  5,  0,-10,
            -10,  0,  5,  0,  0,  0,  0,-10,
            -20,-10,-10, -5, -5,-10,-10,-20
        )
        king.addAll(
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -10,-20,-20,-20,-20,-20,-20,-10,
            20, 20,  0,  0,  0,  0, 20, 20,
            20, 30, 10,  0,  0, 10, 30, 20
        )
        endGameKing.addAll(
            -50,-40,-30,-20,-20,-30,-40,-50,
            -30,-20,-10,  0,  0,-10,-20,-30,
            -30,-10, 20, 30, 30, 20,-10,-30,
            -30,-10, 30, 40, 40, 30,-10,-30,
            -30,-10, 30, 40, 40, 30,-10,-30,
            -30,-10, 20, 30, 30, 20,-10,-30,
            -30,-30,  0,  0,  0,  0,-30,-30,
            -50,-30,-30,-30,-30,-30,-30,-50
        )
    }

    override fun estimateScore(): Int {
        var score = 0
        val pieces = Array<Piece>()
        pieces.addAll(GameBoard.pieces.getPieces(Piece.Color.WHITE))
        pieces.addAll(GameBoard.pieces.getPieces(Piece.Color.BLACK))
        pieces.forEach {
            val x = it.x
            val y = if (it.color == Piece.Color.BLACK) it.y else abs(it.y - 7)
            val value = when(it) {
                is Pawn -> 100 + pawn[y * 8 + x]
                is Bishop -> 330 + bishop[y * 8 + x]
                is Knight -> 320 + knight[y * 8 + x]
                is Rook -> 500 + rook[y * 8 + x]
                is Queen -> 900 + queen[y * 8 + x]
                else -> if (endGame || checkEndGame()) 20000 + endGameKing[y * 8 + x] else 20000 + king[y * 8 + x]
            }
            score += if (it.color == Piece.Color.WHITE) value else -value
        }
        return score
    }

    private fun hasAQueen(side: Piece.Color): Boolean {
        GameBoard.pieces.getPieces(side).forEach {
            if (it is Queen) return true
        }
        return false
    }

    private fun checkEndGame(): Boolean {
        return if (hasAQueen(Piece.Color.WHITE)) {
            if (!hasAQueen(Piece.Color.BLACK)
                    || GameBoard.pieces.getPieces(Piece.Color.BLACK).size < 4
                    || GameBoard.pieces.getPieces(Piece.Color.WHITE).size < 4) {
                endGame = true
                true
            } else false
        } else if (hasAQueen(Piece.Color.BLACK)) {
            if (GameBoard.pieces.getPieces(Piece.Color.BLACK).size < 4) {
                endGame = true
                true
            } else false
        } else {
            endGame = true
            true
        }
    }
}