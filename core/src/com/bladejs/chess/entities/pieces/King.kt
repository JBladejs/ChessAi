package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.bladejs.chess.misc.Position

class King(x: Int, y: Int, color: Color) : Piece(Texture("king.png"), Texture("kingB.png"), x, y, color) {
    override fun getAvailableMoves(): Array<Position> {
        val positions = Array<Position>()
        positions.add(checkForMove(x, y + 1, true))
        positions.add(checkForMove(x + 1, y + 1, true))
        positions.add(checkForMove(x + 1, y, true))
        positions.add(checkForMove(x + 1, y - 1, true))
        positions.add(checkForMove(x, y - 1, true))
        positions.add(checkForMove(x - 1, y - 1, true))
        positions.add(checkForMove(x - 1, y, true))
        positions.add(checkForMove(x - 1, y + 1, true))
        return positions
    }
}