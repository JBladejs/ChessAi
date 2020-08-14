package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.bladejs.chess.misc.Position

class Knight(x: Int, y: Int, color: Color) : Piece(Texture("knight.png"), Texture("knightB.png"), x, y, color, PieceType.KNIGHT) {
    override fun getAvailableMoves(): Array<Position> {
        val positions = Array<Position>()
        positions.add(checkForMove(x - 1, y + 2, true))
        positions.add(checkForMove(x + 1, y + 2, true))
        positions.add(checkForMove(x - 1, y - 2, true))
        positions.add(checkForMove(x + 1, y - 2, true))
        positions.add(checkForMove(x + 2, y - 1, true))
        positions.add(checkForMove(x + 2, y + 1, true))
        positions.add(checkForMove(x - 2, y - 1, true))
        positions.add(checkForMove(x - 2, y + 1, true))
        return positions
    }
}