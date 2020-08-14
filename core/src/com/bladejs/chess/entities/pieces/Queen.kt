package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.bladejs.chess.misc.Position

class Queen(x: Int, y: Int, color: Color) : Piece(Texture("queen.png"), Texture("queenB.png"), x, y, color) {
    override fun getAvailableMoves(): Array<Position> {
        val positions = Array<Position>()
        positions.addAll(checkStraightLinesForMoves())
        positions.addAll(checkDiagonalLinesForMoves())
        return positions
    }
}