package com.bladejs.chess.ai

import com.bladejs.chess.ai.moves.RandomEvaluator
import com.bladejs.chess.handlers.GameHandler
import com.bladejs.chess.ai.board.BoardEvaluator
import com.bladejs.chess.ai.board.NaiveEvaluator
import com.bladejs.chess.ai.moves.MinMaxEvaluator
import com.bladejs.chess.ai.moves.MoveEvaluator

object AiPlayer {
    private const val treeHeight = 0
    val boardEval: BoardEvaluator = NaiveEvaluator
    val moveEval: MoveEvaluator = MinMaxEvaluator(treeHeight)

    fun move() {
        val move = moveEval.getBestMove()
        GameHandler.move(move.fromX, move.fromY, move.toX, move.toY, list = true)
    }
}