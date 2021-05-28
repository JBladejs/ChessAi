package com.bladejs.chess.ai.moves

import com.badlogic.gdx.Game
import com.bladejs.chess.ai.data.MoveNode
import com.bladejs.chess.ai.data.MoveTree
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.pieces.Piece
import com.bladejs.chess.entities.pieces.Rook
import com.bladejs.chess.handlers.GameHandler

//class MinMaxEvaluator(private val treeHeight: Int): MoveEvaluator {
//    val tree = MoveTree(treeHeight)
//
//    override fun getBestMove(): MoveNode {
//        GameBoard.rendering = false
//        GameBoard.pieces.getPieces(GameHandler.currentPlayer).forEach {
////            it.availableMoves.
//        }
//        GameBoard.rendering = true
//    }
//
//
//}