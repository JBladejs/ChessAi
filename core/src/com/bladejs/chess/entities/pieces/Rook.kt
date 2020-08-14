package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array as GdxArray
import com.bladejs.chess.misc.Position

class Rook(x: Int, y: Int, color: Color) : Piece(Texture("rook.png"), Texture("rookB.png"), x, y, color, PieceType.ROOK) {
    override fun getAvailableMoves(): GdxArray<Position> = checkStraightLinesForMoves()
}