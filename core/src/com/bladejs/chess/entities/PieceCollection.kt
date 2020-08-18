package com.bladejs.chess.entities

import com.bladejs.chess.entities.pieces.King
import com.bladejs.chess.entities.pieces.Piece
import com.bladejs.chess.misc.remove
import com.badlogic.gdx.utils.Array as GdxArray

class PieceCollection: Iterable<Piece> {
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

    override fun iterator(): Iterator<Piece> = PieceIterator(this)

    class PieceIterator(private val pieces: PieceCollection): Iterator<Piece> {
        private var i = 0

        override fun hasNext(): Boolean = i < pieces.white.size + pieces.black.size

        override fun next(): Piece {
            if (!hasNext()) throw IllegalStateException()
            val collection = if (i < pieces.white.size) pieces.white else pieces.black
            val nextPiece = collection[i]
            i++
            return nextPiece
        }
    }
}