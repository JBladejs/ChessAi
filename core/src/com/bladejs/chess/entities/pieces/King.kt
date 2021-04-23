package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.misc.Position
import com.bladejs.chess.misc.addValue
import com.badlogic.gdx.utils.Array as GdxArray

class King(x: Int, y: Int, color: Color) : Piece(Texture("king.png"), Texture("kingB.png"), x, y, color) {
    //TODO: check that
    private fun checkForCastle(x: Int, y: Int): Position? {
        if (moveCount == 0) {
            if (x == 2) {
                if (!GameBoard[0][y].isEmpty && GameBoard[1][y].isEmpty && GameBoard[2][y].isEmpty && GameBoard[3][y].isEmpty) {
                    val castlingPiece = GameBoard[0][y].piece!!
                    if (castlingPiece.moveCount == 0 && castlingPiece is Rook && castlingPiece.color == color)
                        return Position(x, y)
                }
            } else if (x == 6) {
                if (!GameBoard[7][y].isEmpty && GameBoard[6][y].isEmpty && GameBoard[5][y].isEmpty) {
                    val castlingPiece = GameBoard[7][y].piece!!
                    if (castlingPiece.moveCount == 0 && castlingPiece is Rook && castlingPiece.color == color)
                        return Position(x, y)
                }
            }
        }
        return null
    }

    override fun getAllMoves(): GdxArray<Position> {
        val positions = GdxArray<Position>()
        positions.addValue(checkForMove(x, y + 1, true))
        positions.addValue(checkForMove(x + 1, y + 1, true))
        positions.addValue(checkForMove(x + 1, y, true))
        positions.addValue(checkForMove(x + 1, y - 1, true))
        positions.addValue(checkForMove(x, y - 1, true))
        positions.addValue(checkForMove(x - 1, y - 1, true))
        positions.addValue(checkForMove(x - 1, y, true))
        positions.addValue(checkForMove(x - 1, y + 1, true))
        positions.addValue(checkForCastle(2, y))
        positions.addValue(checkForCastle(6, y))
        return positions
    }
}