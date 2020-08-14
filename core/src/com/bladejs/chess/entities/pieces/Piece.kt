package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array as GdxArray
import com.bladejs.chess.ChessGame
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.misc.Position

abstract class Piece(private val whiteTexture: Texture, private val blackTexture: Texture, var x: Int, var y: Int, val color: Color) {
    var moveCount = 0
    var draggedX = 0f
    var draggedY = 0f

    enum class Color {
        BLACK, WHITE
    }

    fun canMoveTo(x: Int, y: Int): Boolean {
        getAvailableMoves().forEach {
            if (it.x == x && it.y == y) return true
        }
        return false
    }

    protected fun searchLineForMoves(bottomX: Int, topX: Int, bottomY: Int, topY: Int): GdxArray<Position> = searchLineForMoves(bottomX, topX, bottomY, topY, false)

    protected fun searchLineForMoves(bottomX: Int, topX: Int, bottomY: Int, topY: Int, diagonal: Boolean): GdxArray<Position> {
        val positions = GdxArray<Position>()
        if (bottomX < 0 || bottomX > 7 || bottomY < 0 || bottomY > 7) return positions
        if (topX < 0 || topX > 7 || topY < 0 || topY > 7) return positions
        var i = bottomX
        var j = bottomY
        var iter = 0
        while (iter < 1) {
                if (!diagonal && i == topX && j == topY) iter++
                if (diagonal && (i == topX || j == topY)) iter++
                if (GameBoard[i][j].isEmpty) positions.add(Position(i, j))
                else if (GameBoard[i][j].piece!!.color != color && GameBoard[i][j].isTakeable) {
                    positions.add(Position(i, j))
                    break
                }
            if (topX > i) i++
            if (topX < i) i--
            if (topY > j) j++
            if (topY < j) j--
        }
        return positions
    }

    abstract fun getAvailableMoves(): GdxArray<Position>

    fun render(scale: Float, margin: Float) {
        ChessGame.batch.draw(if (color == Color.WHITE) whiteTexture else blackTexture, (margin + x * scale) + draggedX, (margin + y * scale) + draggedY, scale, scale)
    }
}