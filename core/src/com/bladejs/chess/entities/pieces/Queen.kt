package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.bladejs.chess.misc.Position

class Queen(x: Int, y: Int, color: Color) : Piece(Texture("queen.png"), Texture("queenB.png"), x, y, color) {
    override fun getAvailableMoves(): Array<Position> {
        val positions = Array<Position>()
        positions.addAll(searchLineForMoves(x + 1, 7, y, y))
        positions.addAll(searchLineForMoves(x - 1, 0, y, y))
        positions.addAll(searchLineForMoves(x, x, y + 1, 7))
        positions.addAll(searchLineForMoves(x, x, y - 1, 0))
        positions.addAll(searchLineForMoves(x + 1, 7, y + 1, 7, true))
        positions.addAll(searchLineForMoves(x - 1, 0, y - 1, 0, true))
        positions.addAll(searchLineForMoves(x + 1, 7, y - 1, 0, true))
        positions.addAll(searchLineForMoves(x - 1, 0, y + 1, 7, true))
        return positions
    }
}