package com.bladejs.chess.entities

import com.bladejs.chess.entities.pieces.King
import com.bladejs.chess.entities.pieces.Piece
import com.bladejs.chess.misc.remove
import com.badlogic.gdx.utils.Array as GdxArray

class PieceCollection {
    private val black = GdxArray<Piece>()
    private val white = GdxArray<Piece>()

    fun getPieces(color: Piece.Color) = if (color == Piece.Color.WHITE) white else black

    fun add(piece: Piece) {
        getPieces(piece.color).add(piece)
    }

    fun remove(piece: Piece) {
        getPieces(piece.color).remove(piece)
    }

    fun getKing(color: Piece.Color): Piece {
        val pieces = getPieces(color)
        for (i in 0 until pieces.size) {
            if (pieces[i] is King) return pieces[i]
        }
        throw IllegalStateException("There is no King!!")
    }
}