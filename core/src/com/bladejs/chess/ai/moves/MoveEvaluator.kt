package com.bladejs.chess.ai.moves

import com.bladejs.chess.ai.data.MoveNode

interface MoveEvaluator {
    fun getBestMove(): MoveNode
}