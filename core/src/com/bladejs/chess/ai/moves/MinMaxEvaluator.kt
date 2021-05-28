package com.bladejs.chess.ai.moves

import com.badlogic.gdx.utils.Array
import com.bladejs.chess.ai.AiPlayer
import com.bladejs.chess.ai.data.MoveNode
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.pieces.Piece
import com.bladejs.chess.handlers.GameHandler
import kotlin.math.max
import kotlin.math.min

//TODO: try to simulate another board rather than operating on the main one
class MinMaxEvaluator(private val treeHeight: Int): MoveEvaluator {

    override fun getBestMove(): MoveNode {
        GameBoard.rendering = false
        val move = minimax()
        GameBoard.rendering = true
        return move
    }

    private fun minimax(): MoveNode {
        var node: MoveNode? = null
        var value = Int.MAX_VALUE
        getAvailableMoves(0).forEach { move ->
            val temp = minimax(move)
            if (temp < value) {
                value = temp
                node = move
            }
        }
        return node!!
    }

    private fun minimax(node: MoveNode): Int {
        GameHandler.move(node.fromX, node.fromY, node.toX, node.toY, list = true)
        if (node.depth == treeHeight) {
//            node.value = AiPlayer.boardEval.evaluateBoard()
            val value = AiPlayer.boardEval.evaluateBoard()
            GameHandler.undo(false)
            return value
        }
        if (GameHandler.currentPlayer == Piece.Color.WHITE) { //IF MAX PLAYER
            var value = Int.MIN_VALUE
            getAvailableMoves(node.depth + 1).forEach {
                value = max(value, minimax(it))
            }
            //TODO: check if it should be false or not
            GameHandler.undo(false)
//            node.value = value
            return value
        } else { //IF MIN PLAYER
            var value = Int.MAX_VALUE
            getAvailableMoves(node.depth + 1).forEach {
                value = min(value, minimax(it))
            }
            //TODO: check if it should be false or not
            GameHandler.undo(false)
//            node.value = value
            return value
        }
    }
}