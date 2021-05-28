package com.bladejs.chess.ai.moves

import com.badlogic.gdx.utils.Array
import com.bladejs.chess.ai.data.MoveNode
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.handlers.GameHandler
import kotlin.random.Random

class RandomEvaluator: MoveEvaluator {
    override fun getBestMove(): MoveNode {
        val moves = Array<MoveNode>()
        GameBoard.pieces.getPieces(GameHandler.currentPlayer).forEach { piece ->
            piece.availableMoves.forEach { move ->
                moves.add(MoveNode(0,piece,move.x,move.y))
            }
        }
        return moves[Random.nextInt(moves.size)]
    }

}