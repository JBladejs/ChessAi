package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture

class Knight(x: Int, y: Int, color: Color) : Piece(Texture("knight.png"), Texture("knightB.png"), x, y, color) {
    override fun canMoveTo(x: Int, y: Int): Boolean = true
}