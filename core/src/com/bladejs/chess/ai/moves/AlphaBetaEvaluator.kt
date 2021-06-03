package com.bladejs.chess.ai.moves

import com.bladejs.chess.ai.AiPlayer
import com.bladejs.chess.ai.data.MoveNode
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.pieces.Piece
import com.bladejs.chess.handlers.GameHandler
import com.bladejs.chess.misc.GameState
import kotlin.math.max
import kotlin.math.min

class AlphaBetaEvaluator(private val treeHeight: Int): MoveEvaluator {
//    var alpha = Int.MIN_VALUE
//    var beta = Int.MAX_VALUE

    override fun getBestMove(): MoveNode {
        GameBoard.rendering = false
        val move = alphaBeta()
        GameBoard.rendering = true
        return move
    }

    private fun alphaBeta(): MoveNode {
        val alpha = Int.MIN_VALUE
        var beta = Int.MAX_VALUE
        var node: MoveNode? = null
        var value = Int.MAX_VALUE
        for (move in getAvailableMoves(0)) {
            val temp = alphaBeta(move, alpha, beta)
            if (temp < value) {
                value = temp
                node = move
                if (value < beta) beta = value
                if (value < alpha) break
            }
        }
        return node!!
    }

    private fun alphaBeta(node: MoveNode, a: Int, b: Int): Int {
        var alpha = a
        var beta = b
        GameHandler.move(node.fromX, node.fromY, node.toX, node.toY, list = true)
        if (node.depth == treeHeight) {
//            node.value = AiPlayer.boardEval.evaluateBoard()
            val value = AiPlayer.boardEval.evaluateBoard()
            GameHandler.undo(false)
            return value
        }
        val state = GameHandler.checkForMate(true)
        if (state == GameState.WHITE_WON || state == GameState.BLACK_WON || state == GameState.DRAW) {
            GameHandler.undo()
            return state.score
        }
        if (GameHandler.currentPlayer == Piece.Color.WHITE) { //IF MAX PLAYER
            for (it in getAvailableMoves(node.depth + 1)) {
                alpha = max(alpha, alphaBeta(it, alpha, beta))
                if (alpha >= beta) break
            }
            GameHandler.undo(false)
            return alpha
        } else { //IF MIN PLAYER
            for (it in getAvailableMoves(node.depth + 1)) {
                beta = min(beta, alphaBeta(it, alpha, beta))
                if (alpha >= beta) break
            }
            //TODO: check if it should be false or not
            GameHandler.undo(false)
            return beta
        }
    }
}