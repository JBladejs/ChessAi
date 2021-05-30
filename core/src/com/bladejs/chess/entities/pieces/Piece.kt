package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.bladejs.chess.ChessGame
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.handlers.GameHandler
import com.bladejs.chess.misc.IntColor
import com.bladejs.chess.misc.Position
import com.bladejs.chess.misc.addValue
import kotlin.math.abs
import com.badlogic.gdx.utils.Array as GdxArray

//TODO: generate available moves on the start of a turn
//TODO: implement move highlighting
//TODO: refactor this
abstract class Piece(private val whiteTexture: Texture, private val blackTexture: Texture, var x: Int, var y: Int, val color: Color) : Cloneable {
    var moveCount = 0
    var draggedX = 0f
    var draggedY = 0f
    val availableMoves = GdxArray<Position>()

    enum class Color {
        BLACK, WHITE
    }

    public override fun clone(): Piece = super.clone() as Piece

    //TODO: change that to boolean
    protected fun checkForMove(x: Int, y: Int, inclTakes: Boolean = true): Position? {
        if (x !in 0..7 || y !in 0..7) return null
        return if (GameBoard[x][y].isEmpty) Position(x, y)
        else if (inclTakes && color != GameBoard[x][y].piece!!.color) Position(x, y)
        else null
    }

    //TODO: change that to boolean
    protected fun checkForTake(x: Int, y: Int): Position? {
        if (x !in 0..7 || y !in 0..7) return null
        return if (!GameBoard[x][y].isEmpty && color != GameBoard[x][y].piece!!.color) Position(x, y) else null
    }

    //TODO: add in-function diagonal checking
    private fun searchLineForMoves(bottomX: Int, topX: Int, bottomY: Int, topY: Int, diagonal: Boolean = false): GdxArray<Position> {
        val positions = GdxArray<Position>()
        if (bottomX < 0 || bottomX > 7 || bottomY < 0 || bottomY > 7) return positions
        if (topX < 0 || topX > 7 || topY < 0 || topY > 7) return positions
        var i = bottomX
        var j = bottomY
        while (true) {
            val position = checkForMove(i, j)
            if (position != null) {
                positions.add(position)
                if (checkForTake(i, j) != null) break
            }
            else break
            if (!diagonal && i == topX && j == topY) break
            if (diagonal && (i == topX || j == topY)) break
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
        val positions = GdxArray<Position>()
        positions.addAll(searchLineForMoves(x + 1, 7, y + 1, 7, true))
        positions.addAll(searchLineForMoves(x - 1, 0, y - 1, 0, true))
        positions.addAll(searchLineForMoves(x + 1, 7, y - 1, 0, true))
        positions.addAll(searchLineForMoves(x - 1, 0, y + 1, 7, true))
        return positions
    }

    protected abstract fun getAllMoves(): GdxArray<Position>

    //TODO: refactor undoing and foresight
    fun getAvailableMoves(foresight: Boolean = true, ignoreTurn: Boolean = false): GdxArray<Position> {
        val positions = GdxArray<Position>()
        if (ignoreTurn || GameHandler.currentPlayer == color) {
            if (foresight) {
                GameBoard.rendering = false
                GameBoard.remove(this)
                GameBoard.add(this.clone())
                var temp: Piece? = null
                getAllMoves().forEach {
                    //TODO: find a better way to fix this
                    if (this is King && abs(x - it.x) > 1) {
                        temp = if (x == 2) GameBoard[0][y].piece!!
                        else GameBoard[7][y].piece!!
                        GameBoard.remove(temp!!)
                        GameBoard.add(temp!!.clone())
                    }
                    GameHandler.move(this.x, this.y, it.x, it.y, false)
                    if (!GameHandler.checkForCheck(color)){
                        positions.add(it)
                    }
                    GameHandler.undo()
                    if (temp != null) {
                        GameBoard.remove(GameBoard[temp!!.x][temp!!.y].piece!!)
                        GameBoard.add(temp!!)
                    }
                }
                GameBoard.remove(GameBoard[this.x][this.y].piece!!)
                GameBoard.add(this)
                GameBoard.rendering = true
            } else return getAllMoves()
        }
        return positions
    }

    fun generateAvailableMoves() {
        availableMoves.clear()
        availableMoves.addAll(getAvailableMoves())
    }

    //TODO: remove piece color checking for greater code readability
    //TODO: try to remove list argument
    fun canMoveTo(x: Int, y: Int, list: Boolean = false, foresight: Boolean = true, ignoreTurn: Boolean = false): Boolean {
        val moves = if (list) availableMoves else getAvailableMoves(foresight, ignoreTurn)
        moves.forEach {
            if (it?.x == x && it.y == y) return true
        }
        return false
    }

    //TODO: merge render methods
    fun render(scale: Float, margin: Float) = ChessGame.batch.draw(if (color == Color.WHITE) whiteTexture else blackTexture, (margin + x * scale) + draggedX, (margin + y * scale) + draggedY, scale, scale)

    fun renderPrecise(x: Float, y: Float, scale: Float) {
        ChessGame.batch.draw(if (color == Color.WHITE) whiteTexture else blackTexture, x, y, scale, scale)
    }
}