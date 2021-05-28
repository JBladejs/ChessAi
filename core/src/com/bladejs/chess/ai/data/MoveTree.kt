package com.bladejs.chess.ai.data

import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.pieces.Piece

class MoveTree(val maxHeight: Int) {
    var height: Int = 0

    private fun addLevel() = height++

    private fun addNode(movingPiece: Piece, toX: Int, toY: Int) {
        if (height == 0) addLevel()

    }
}