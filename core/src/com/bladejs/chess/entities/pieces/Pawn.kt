package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.misc.Position

class Pawn(x: Int, y: Int, color: Color) : Piece(Texture("pawn.png"), Texture("pawnB.png"), x, y, color) {
    var movedTwoFields = false
    override fun getAvailableMoves(): Array<Position> {
        val positions = Array<Position>()
        var yIncrease = 1
        if (color == Color.BLACK) yIncrease = -1
        if (y + yIncrease in 0..7) {
            positions.add(checkForMove(x, y + yIncrease, false))
            positions.add(checkForTake(x - 1, y + yIncrease))
            positions.add(checkForTake(x + 1, y + yIncrease))
            positions.add(checkForEnPassant(x - 1, y))
            positions.add(checkForEnPassant(x + 1, y))
        }
        if (moveCount == 0) positions.add(checkForMove(x, y + (yIncrease * 2), false))
        return positions
    }

    private fun checkForEnPassant(x: Int, y: Int): Position? {
        if (x !in 0..7 || y !in 0..7) return null
        if (!GameBoard[x][y].isTakable || GameBoard[x][y].piece!!.color == color) return null
        val passingPiece = GameBoard[x][y].piece
        if (passingPiece is Pawn &&  passingPiece.moveCount == 1 && passingPiece.movedTwoFields)
            return if (color == Color.BLACK) Position(x, y - 1) else Position(x, y + 1)
        return null
    }
}