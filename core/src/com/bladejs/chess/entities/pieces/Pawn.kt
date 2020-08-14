package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.misc.Position

class Pawn(x: Int, y: Int, color: Color) : Piece(Texture("pawn.png"), Texture("pawnB.png"), x, y, color) {
    override fun getAvailableMoves(): Array<Position> {
        val positions = Array<Position>()
        var yIncrease = 1
        if (color == Color.BLACK) yIncrease = -1
        if (y + yIncrease in 0..7) {
            if (GameBoard[x][y + yIncrease].isEmpty) positions.add(Position(x, y + yIncrease))
            if (x - 1 >= 0 && GameBoard[x - 1][y + yIncrease].isTakeable && GameBoard[x - 1][y + yIncrease].piece!!.color != color)
                positions.add(Position(x - 1, y + yIncrease))
            if (x + 1 <= 7 && GameBoard[x + 1][y + yIncrease].isTakeable && GameBoard[x + 1][y + yIncrease].piece!!.color != color)
                positions.add(Position(x + 1, y + yIncrease))
        }
        if (moveCount == 0 && GameBoard[x][y + (yIncrease * 2)].isEmpty) positions.add(Position(x, y + (yIncrease * 2)))
        return positions
    }
}