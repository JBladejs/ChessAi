package com.bladejs.chess.misc

import com.bladejs.chess.entities.pieces.Piece
import com.badlogic.gdx.utils.Array as GdxArray

data class Move(val types: GdxArray<Type>, val positions: GdxArray<Position>, val pieces: GdxArray<Piece>, val fieldFrom: Pair<Int, Int>?, val fieldTo: Pair<Int, Int>?) {
    enum class Type {
        REMOVE, ADD
    }
}