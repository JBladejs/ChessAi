package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture

class King(x: Int, y: Int, color: Color) : Piece(Texture("king.png"), Texture("kingB.png"), x, y, color) {
    override fun canMoveTo(x: Int, y: Int): Boolean = true
}