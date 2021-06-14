package com.bladejs.chess.ai.moves

import com.bladejs.chess.ai.AiPlayer
import com.bladejs.chess.ai.data.MoveNode
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.pieces.Piece
import com.bladejs.chess.entities.pieces.Rook
import com.bladejs.chess.handlers.GameHandler
import com.bladejs.chess.misc.GameState
import kotlin.math.max
import kotlin.math.min

class AlphaBetaEvaluator(private val treeHeight: Int): MoveEvaluator {
//    var alpha = Int.MIN_VALUE
//    var beta = Int.MAX_VALUE
//    TODO: rewrite whole AlphaBeta
    var debug = 0

    override fun getBestMove(): MoveNode {
        GameBoard.rendering = false
        val move = alphaBeta(treeHeight)
        GameBoard.rendering = true
        return move
    }

    private fun alphaBeta(depth: Int): MoveNode {
        val alpha = Int.MIN_VALUE
        var beta = Int.MAX_VALUE
        var node: MoveNode? = null
        var value = Int.MAX_VALUE
        val moves = getAvailableMoves()
        for (move in moves) {
            val temp = alphaBeta(depth - 1, move, alpha, beta)
            if (temp < value) {
                value = temp
                node = move
                if (value < beta) beta = value
                if (value < alpha) break
            }
        }
        return node!!
    }

    private fun alphaBeta(depth: Int, node: MoveNode, a: Int, b: Int): Int {
        var alpha = a
        var beta = b
        GameHandler.move(node.fromX, node.fromY, node.toX, node.toY, list = true)
        if (depth == 0) {
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
        val moves = getAvailableMoves()
        if (GameHandler.currentPlayer == Piece.Color.WHITE) { //IF MAX PLAYER
            for (it in moves) {
                alpha = max(alpha, alphaBeta(depth - 1, it, alpha, beta))
                if (alpha >= beta) break
            }
            GameHandler.undo(false)
            return alpha
        } else { //IF MIN PLAYER
            for (it in moves) {
                beta = min(beta, alphaBeta(depth -1, it, alpha, beta))
                if (alpha >= beta) break
            }
            //TODO: check if it should be false or not
            GameHandler.undo(false)
            return beta
        }
    }
}