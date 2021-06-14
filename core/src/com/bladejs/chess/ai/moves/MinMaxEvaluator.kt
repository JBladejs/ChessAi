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
        val move = minimax(treeHeight)
        GameBoard.rendering = true
        return move
    }

    private fun minimax(depth: Int): MoveNode {
        var node: MoveNode? = null
        var value = Int.MAX_VALUE
        getAvailableMoves().forEach { move ->
            val temp = minimax(depth - 1, move)
            println(temp)
            if (temp < value) {
                value = temp
                node = move
            }
        }
        println()
        println(value)
        return node!!
    }

    private fun minimax(depth: Int, node: MoveNode): Int {
        GameHandler.move(node.fromX, node.fromY, node.toX, node.toY, list = true)
        if (depth == 0) {
//            node.value = AiPlayer.boardEval.evaluateBoard()
            val value = AiPlayer.boardEval.evaluateBoard()
            GameHandler.undo(false)
            return value
        }
        val moves = getAvailableMoves()
        if (GameHandler.currentPlayer == Piece.Color.WHITE) { //IF MAX PLAYER
            var value = Int.MIN_VALUE
            moves.forEach {
                value = max(value, minimax(depth - 1, it))
            }
            //TODO: check if it should be false or not
            GameHandler.undo(false)
//            node.value = value
            return value
        } else { //IF MIN PLAYER
            var value = Int.MAX_VALUE
            moves.forEach {
                value = min(value, minimax(depth - 1, it))
            }
            //TODO: check if it should be false or not
            GameHandler.undo(false)
//            node.value = value
            return value
        }
    }
}