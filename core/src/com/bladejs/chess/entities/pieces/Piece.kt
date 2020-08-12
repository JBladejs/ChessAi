package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.bladejs.chess.ChessGame
import com.bladejs.chess.misc.Position

abstract class Piece(val texture: Texture, var x: Int, var y: Int) {
    val position = Position(x, y)

    fun render(scale: Float, margin: Float) {
        ChessGame.batch.draw(texture, margin + x * scale, margin + y * scale, scale, scale)
    }
}