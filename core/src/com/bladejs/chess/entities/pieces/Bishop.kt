package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array as GdxArray
import com.bladejs.chess.misc.Position

class Bishop(x: Int, y: Int, color: Color) : Piece(Texture("bishop.png"), Texture("bishopB.png"), x, y, color) {
    override fun getAvailableMoves(): GdxArray<Position> = checkDiagonalLinesForMoves()
}