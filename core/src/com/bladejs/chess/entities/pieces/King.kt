package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array as GdxArray
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.misc.Position

class King(x: Int, y: Int, color: Color) : Piece(Texture("king.png"), Texture("kingB.png"), x, y, color) {
    override fun getAvailableMoves(): GdxArray<Position> {
        val positions = GdxArray<Position>()
        positions.add(checkForMove(x, y + 1, true))
        positions.add(checkForMove(x + 1, y + 1, true))
        positions.add(checkForMove(x + 1, y, true))
        positions.add(checkForMove(x + 1, y - 1, true))
        positions.add(checkForMove(x, y - 1, true))
        positions.add(checkForMove(x - 1, y - 1, true))
        positions.add(checkForMove(x - 1, y, true))
        positions.add(checkForMove(x - 1, y + 1, true))
        positions.add(checkForCastle(2, y))
        positions.add(checkForCastle(6, y))
        return positions
    }

    private fun checkForCastle(x: Int, y: Int): Position? {
        if (moveCount == 0) {
            if (x == 2) {
                if (!GameBoard[0][y].isEmpty && GameBoard[1][y].isEmpty && GameBoard[2][y].isEmpty && GameBoard[3][y].isEmpty) {
                    val castlingPiece = GameBoard[0][y].piece!!
                    if (castlingPiece.moveCount == 0 && castlingPiece is Rook && castlingPiece.color == color)
                        return Position(x, y)
                }
            } else if (x == 6) {
                if (!GameBoard[7][y].isEmpty  && GameBoard[6][y].isEmpty && GameBoard[5][y].isEmpty) {
                    val castlingPiece = GameBoard[7][y].piece!!
                    if (castlingPiece.moveCount == 0 && castlingPiece is Rook && castlingPiece.color == color)
                        return Position(x, y)
                }
            }
        }
        return null
    }
}