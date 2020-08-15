package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.bladejs.chess.ChessGame
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.misc.Position
import com.badlogic.gdx.utils.Array as GdxArray

abstract class Piece(private val whiteTexture: Texture, private val blackTexture: Texture, var x: Int, var y: Int, val color: Color) : Cloneable {
    var moveCount = 0
    var draggedX = 0f
    var draggedY = 0f

    enum class Color {
        BLACK, WHITE
    }

    public override fun clone(): Piece {
        return super.clone() as Piece
    }

    protected fun checkForMove(x: Int, y: Int, inclTakes: Boolean): Position? {
        if (x !in 0..7 || y !in 0..7) return null
        return if (GameBoard[x][y].isEmpty) Position(x, y)
        else if (inclTakes && GameBoard[x][y].isTakable && color != GameBoard[x][y].piece!!.color) Position(x, y)
        else null
    }

    protected fun checkForTake(x: Int, y: Int): Position? {
        if (x !in 0..7 || y !in 0..7) return null
        return if (GameBoard[x][y].isTakable && color != GameBoard[x][y].piece!!.color) Position(x, y) else null
    }

    private fun searchLineForMoves(bottomX: Int, topX: Int, bottomY: Int, topY: Int): GdxArray<Position> = searchLineForMoves(bottomX, topX, bottomY, topY, false)

    private fun searchLineForMoves(bottomX: Int, topX: Int, bottomY: Int, topY: Int, diagonal: Boolean): GdxArray<Position> {
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
            else if (GameBoard[i][j].piece!!.color != color && GameBoard[i][j].isTakable) {
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

    protected fun checkStraightLinesForMoves(): GdxArray<Position> {
        val positions = GdxArray<Position>()
        positions.addAll(searchLineForMoves(x + 1, 7, y, y))
        positions.addAll(searchLineForMoves(x - 1, 0, y, y))
        positions.addAll(searchLineForMoves(x, x, y + 1, 7))
        positions.addAll(searchLineForMoves(x, x, y - 1, 0))
        return positions
    }

    protected fun checkDiagonalLinesForMoves(): GdxArray<Position> {
        val positions = com.badlogic.gdx.utils.Array<Position>()
        positions.addAll(searchLineForMoves(x + 1, 7, y + 1, 7, true))
        positions.addAll(searchLineForMoves(x - 1, 0, y - 1, 0, true))
        positions.addAll(searchLineForMoves(x + 1, 7, y - 1, 0, true))
        positions.addAll(searchLineForMoves(x - 1, 0, y + 1, 7, true))
        return positions
    }

    abstract fun getAvailableMoves(): GdxArray<Position>

    fun canMoveTo(x: Int, y: Int): Boolean {
        getAvailableMoves().forEach {
            if (it?.x == x && it.y == y) return true
        }
        return false
    }

    fun render(scale: Float, margin: Float) {
        ChessGame.batch.draw(if (color == Color.WHITE) whiteTexture else blackTexture, (margin + x * scale) + draggedX, (margin + y * scale) + draggedY, scale, scale)
    }

    fun renderPrecise(x: Float, y: Float, scale: Float) {
        ChessGame.batch.draw(if (color == Color.WHITE) whiteTexture else blackTexture, x, y, scale, scale)
    }
}