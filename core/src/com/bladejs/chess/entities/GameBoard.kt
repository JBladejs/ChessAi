package com.bladejs.chess.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Array
import com.bladejs.chess.ChessGame
import com.bladejs.chess.entities.pieces.*
import com.bladejs.chess.entities.pieces.Piece.Color.*

class GameBoard {
    private val board = Array(8) { Array<BoardField>(8) }
    private val pieces = ArrayList<Piece>()

    init {
        with(pieces) {
            for (i in 0..7) add(Pawn(i, 1, WHITE))
            for (i in 0..7) add(Pawn(i, 6, BLACK))
            add(King(4, 0, WHITE))
            add(Queen(3, 0, WHITE))
            add(Rook(0, 0, WHITE))
            add(Rook(7, 0, WHITE))
            add(Bishop(2, 0, WHITE))
            add(Bishop(5, 0, WHITE))
            add(Knight(1, 0, WHITE))
            add(Knight(6, 0, WHITE))
            add(King(4, 7, BLACK))
            add(Queen(3, 7, BLACK))
            add(Rook(0, 7, BLACK))
            add(Rook(7, 7, BLACK))
            add(Bishop(2, 7, BLACK))
            add(Bishop(5, 7, BLACK))
            add(Knight(1, 7, BLACK))
            add(Knight(6, 7, BLACK))
        }
    }

    operator fun get(i: Int) = board[i]
    fun render() {
        val cellSize = Gdx.graphics.height / 9f
        val halfCellSize = cellSize / 2f
        val doubleCellSize = cellSize * 2f
        val quarterCellSize = halfCellSize / 2f
        with(ChessGame.renderer) {
            begin(ShapeRenderer.ShapeType.Filled)
            color.set(Color.BROWN)
            rect(0f, 0f, halfCellSize, Gdx.graphics.height.toFloat())
            rect(0f, 0f, Gdx.graphics.width.toFloat(), halfCellSize)
            rect(0f, Gdx.graphics.height.toFloat() - halfCellSize, Gdx.graphics.width.toFloat(), halfCellSize)
            rect(Gdx.graphics.width.toFloat() - halfCellSize, 0f, halfCellSize, Gdx.graphics.height.toFloat())
            color.set(Color.WHITE)
            for (i in 0..7) {
                for (j in 0..3) {
                    val start = if (i % 2 == 0) cellSize + halfCellSize else halfCellSize
                    rect(start + (j * doubleCellSize), halfCellSize + (i * cellSize), cellSize, cellSize)
                }
            }
            end()
        }
        with(ChessGame.batch) {
            begin()
            for (i in 0..1) {
                var character = 'a'
                val horizontalPlacement = if (i == 0) quarterCellSize + ChessGame.font.capHeight else Gdx.graphics.height - quarterCellSize
                for (j in 1..8) ChessGame.font.draw(this, (character++).toString(), j * cellSize, horizontalPlacement)
            }
            for (i in 0..1) {
                var character = 1
                val verticalPlacement = if (i == 0) quarterCellSize else Gdx.graphics.height - quarterCellSize
                for (j in 1..8) ChessGame.font.draw(this, (character++).toString(), verticalPlacement, j * cellSize)
            }

            pieces.forEach { it.render(cellSize, halfCellSize) }
            end()
        }
    }
}