package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array as GdxArray
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.misc.Position

class Rook(x: Int, y: Int, color: Color) : Piece(Texture("rook.png"), Texture("rookB.png"), x, y, color) {
    override fun getAvailableMoves(): GdxArray<Position> {
        val array = GdxArray<Position>()
        for (i in 1..4) {
            var topLimit = 0
            var bottomLimit = 0
            when (i) {
                1 -> {
                    bottomLimit = x + 1
                    topLimit = 7
                }
                2 -> {
                    bottomLimit = x - 1
                    topLimit = 0
                }
                3 -> {
                    bottomLimit = y + 1
                    topLimit = 7
                }
                4 -> {
                    bottomLimit = y - 1
                    topLimit = 0
                }
            }
            if (bottomLimit < 0 || bottomLimit > 7) continue
            for (j in bottomLimit..topLimit) {
                if (i < 3) {
                    if (GameBoard[j][y].isEmpty) array.add(Position(j, y))
                    else if (GameBoard[j][y].piece!!.color == Color.BLACK && GameBoard[j][y].piece !is King) {
                        array.add(Position(j, y))
                        break
                    }
                } else {
                    if (GameBoard[x][j].isEmpty) array.add(Position(x, j))
                    else if (GameBoard[x][j].piece!!.color == Color.BLACK && GameBoard[x][j].piece !is King) {
                        array.add(Position(x, j))
                        break
                    }
                }
            }
        }
        return array
    }
}