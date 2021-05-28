package com.bladejs.chess.ai.moves

import com.badlogic.gdx.utils.Array
import com.bladejs.chess.ai.data.MoveNode
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.handlers.GameHandler
import kotlin.random.Random

class RandomEvaluator: MoveEvaluator {
    override fun getBestMove(): MoveNode {
        val moves = getAvailableMoves(0)
        return moves[Random.nextInt(moves.size)]
    }

}