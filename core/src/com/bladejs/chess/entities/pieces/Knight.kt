package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.bladejs.chess.misc.Position

class Knight(x: Int, y: Int, color: Color) : Piece(Texture("knight.png"), Texture("knightB.png"), x, y, color) {
    override fun getAvailableMoves(): Array<Position> {
        TODO("Not yet implemented")
    }
}