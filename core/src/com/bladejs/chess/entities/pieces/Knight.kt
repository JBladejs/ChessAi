package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.bladejs.chess.misc.Position
import com.bladejs.chess.misc.addValue

class Knight(x: Int, y: Int, color: Color) : Piece(Texture("knight.png"), Texture("knightB.png"), x, y, color) {
    override fun getAllMoves(): Array<Position> {
        val positions = Array<Position>()
        positions.addValue(checkForMove(x - 1, y + 2, true))
        positions.addValue(checkForMove(x + 1, y + 2, true))
        positions.addValue(checkForMove(x - 1, y - 2, true))
        positions.addValue(checkForMove(x + 1, y - 2, true))
        positions.addValue(checkForMove(x + 2, y - 1, true))
        positions.addValue(checkForMove(x + 2, y + 1, true))
        positions.addValue(checkForMove(x - 2, y - 1, true))
        positions.addValue(checkForMove(x - 2, y + 1, true))
        return positions
    }
}