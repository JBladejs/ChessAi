package com.bladejs.chess.ai

import com.bladejs.chess.ai.board.NaiveEvaluator
import com.bladejs.chess.ai.moves.MinMaxEvaluator
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.pieces.Piece

class AiPlayer {
    val boardEval = NaiveEvaluator
    val moveEval = MinMaxEvaluator
}