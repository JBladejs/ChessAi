package com.bladejs.chess.entities

import com.bladejs.chess.entities.pieces.King
import com.bladejs.chess.entities.pieces.Piece

class BoardField {
    var piece: Piece? = null
    val isEmpty: Boolean
        get() = piece == null
    val isTakable: Boolean
        get() = !isEmpty && piece !is King
}