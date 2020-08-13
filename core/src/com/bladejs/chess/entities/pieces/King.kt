package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.bladejs.chess.misc.Position

class King(x: Int, y: Int, color: Color) : Piece(Texture("king.png"), Texture("kingB.png"), x, y, color) {
    override fun getAvailableMoves(): Array<Position> {
        TODO("Not yet implemented")
    }
}