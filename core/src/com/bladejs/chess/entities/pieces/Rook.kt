package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array as GdxArray
import com.bladejs.chess.misc.Position

class Rook(x: Int, y: Int, color: Color) : Piece(Texture("rook.png"), Texture("rookB.png"), x, y, color) {
    override fun getAvailableMoves(): GdxArray<Position> {
        val positions = GdxArray<Position>()
        positions.addAll(searchLineForMoves(x + 1, 7, y, y))
        positions.addAll(searchLineForMoves(x - 1, 0, y, y))
        positions.addAll(searchLineForMoves(x, x, y + 1, 7))
        positions.addAll(searchLineForMoves(x, x, y - 1, 0))
        return positions
    }
}