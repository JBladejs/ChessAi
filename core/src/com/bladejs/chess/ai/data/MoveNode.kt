package com.bladejs.chess.ai.data

import com.badlogic.gdx.utils.Array
import com.bladejs.chess.entities.pieces.Piece

data class MoveNode(val depth: Int, val fromX: Int, val fromY: Int, val toX: Int, val toY: Int/*, val parent: MoveNode? = null*/) {
//    constructor(parent: MoveNode, movingPiece: Piece, toX: Int, toY: Int) : this(parent.depth + 1, movingPiece, toX, toY, parent)
//    var value: Int? = null
//    val children = Array<MoveNode>()
}