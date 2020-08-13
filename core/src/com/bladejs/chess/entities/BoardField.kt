package com.bladejs.chess.entities

import com.bladejs.chess.entities.pieces.Piece

class BoardField {
    var piece: Piece? = null
    val isEmpty : Boolean
    get() = piece != null
}