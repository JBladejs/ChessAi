package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.bladejs.chess.misc.Position
import com.badlogic.gdx.utils.Array as GdxArray

class Rook(x: Int, y: Int, color: Color) : Piece(Texture("rook.png"), Texture("rookB.png"), x, y, color) {
    override fun getAllMoves(): GdxArray<Position> = checkStraightLinesForMoves()
}