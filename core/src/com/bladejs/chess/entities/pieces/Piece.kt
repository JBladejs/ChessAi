package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.bladejs.chess.ChessGame

abstract class Piece(private val whiteTexture: Texture, private val blackTexture: Texture, var x: Int, var y: Int, val color: Color) {
    enum class Color {
        BLACK, WHITE
    }

    abstract fun canMoveTo(x: Int, y: Int): Boolean

    fun render(scale: Float, margin: Float) {
        ChessGame.batch.draw(if (color == Color.WHITE) whiteTexture else blackTexture, margin + x * scale, margin + y * scale, scale, scale)
    }
}