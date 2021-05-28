package com.bladejs.chess.ai.moves

import com.badlogic.gdx.utils.Array
import com.bladejs.chess.ai.data.MoveNode
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.handlers.GameHandler
import com.bladejs.chess.misc.Move

//TODO: try to remove MoveNode and operate on pure data
interface MoveEvaluator {
    fun getBestMove(): MoveNode
    fun getAvailableMoves(depth: Int): Array<MoveNode> {
        val moves = Array<MoveNode>()
        GameBoard.pieces.getPieces(GameHandler.currentPlayer).forEach { piece ->
            piece.availableMoves.forEach { move ->
                moves.add(MoveNode(depth, piece.x, piece.y, move.x, move.y))
            }
        }
        return moves
    }
}