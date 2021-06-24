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
//        if (node.fromX == 4 && node.fromY == 0 && node.toX == 0 && node.toY == 0) {
//            println("debug")
//        }
        //TODO: find the source of the problem
        GameHandler.move(node.fromX, node.fromY, node.toX, node.toY, list = true, moveGeneration = depth != 0)
        val state = GameHandler.checkForMate(true)
        if (depth == 0 || state == GameState.WHITE_WON || state == GameState.BLACK_WON || state == GameState.DRAW) {
            var value = AiPlayer.boardEval.evaluateBoard(state)
            if (state == GameState.BLACK_WON) value -= depth
            GameHandler.undo(false)
            return value
        }
        val moves = getAvailableMoves()
        if (GameHandler.currentPlayer == Piece.Color.WHITE) { //IF MAX PLAYER
            for (it in moves) {
                val value = alphaBeta(depth - 1, it, alpha, beta)
                alpha = max(alpha, value)
                if (alpha >= beta) break
            }
            GameHandler.undo(false)
            return alpha
        } else { //IF MIN PLAYER
            for (it in moves) {
                val value = alphaBeta(depth -1, it, alpha, beta)
                beta = min(beta, value)
                if (alpha >= beta) break
            }
            //TODO: check if it should be false or not
            GameHandler.undo(false)
            return beta
        }
    }
}