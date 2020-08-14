package com.bladejs.chess.handlers

import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.pieces.*
import com.bladejs.chess.misc.Move
import com.bladejs.chess.misc.Position
import com.bladejs.chess.misc.clone
import com.badlogic.gdx.utils.Array as GdxArray

object GameHandler {
    private val moves = GdxArray<Move>()
    private val currentMoveTypes = GdxArray<Move.Type>()
    private val currentPieceTypes = GdxArray<Piece.PieceType>()
    private val currentPositions = GdxArray<Position>()

    fun appendToMove(moveType: Move.Type, position: Position, pieceTypeType: Piece.PieceType) {
        currentMoveTypes.add(moveType)
        currentPositions.add(position)
        currentPieceTypes.add(pieceTypeType)
    }

    fun confirmMove(color: Piece.Color) {
        moves.add(Move(currentMoveTypes.clone(), currentPositions.clone(), currentPieceTypes.clone(), color))
        deleteMove()
    }

    fun deleteMove() {
        currentPositions.clear()
        currentMoveTypes.clear()
        currentPieceTypes.clear()
    }

    fun undoMove() {
        val lastMove = moves[moves.size - 1]
        for (i in lastMove.types.size - 1 downTo 0) {
            if (lastMove.types[i] == Move.Type.ADD) GameBoard.remove(GameBoard[lastMove.positions[i].x][lastMove.positions[i].y].piece!!)
            if (lastMove.types[i] == Move.Type.REMOVE) {
                val color = if (i == 0 && lastMove.types.size % 2 != 0)
                    if (lastMove.color == Piece.Color.BLACK) Piece.Color.WHITE
                    else Piece.Color.BLACK
                else lastMove.color
                val addedPiece: Piece = when (lastMove.pieces[i]) {
                    Piece.PieceType.BISHOP -> Bishop(lastMove.positions[i].x, lastMove.positions[i].y, color)
                    Piece.PieceType.KING -> King(lastMove.positions[i].x, lastMove.positions[i].y, color)
                    Piece.PieceType.KNIGHT -> Knight(lastMove.positions[i].x, lastMove.positions[i].y, color)
                    Piece.PieceType.PAWN -> Pawn(lastMove.positions[i].x, lastMove.positions[i].y, color)
                    Piece.PieceType.QUEEN -> Queen(lastMove.positions[i].x, lastMove.positions[i].y, color)
                    Piece.PieceType.ROOK -> Rook(lastMove.positions[i].x, lastMove.positions[i].y, color)
                    null -> throw IllegalAccessException()
                }
                GameBoard.add(addedPiece)
            }
        }
        moves.removeIndex(moves.size - 1)
        deleteMove()
    }
}