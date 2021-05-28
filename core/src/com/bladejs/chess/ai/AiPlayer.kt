package com.bladejs.chess.ai

import com.bladejs.chess.ai.moves.RandomEvaluator
import com.bladejs.chess.handlers.GameHandler
import com.bladejs.chess.ai.board.BoardEvaluator
import com.bladejs.chess.ai.board.NaiveEvaluator
import com.bladejs.chess.ai.data.MoveTree
import com.bladejs.chess.ai.moves.MoveEvaluator
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.pieces.Piece

object AiPlayer {
    private const val treeHeight = 1
    val boardEval: BoardEvaluator = NaiveEvaluator
    val moveEval: MoveEvaluator = RandomEvaluator()

    fun move() {
        val move = moveEval.getBestMove()
        GameHandler.move(move.movingPiece, move.toX, move.toY, list = true)
    }
}