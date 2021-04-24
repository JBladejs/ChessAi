package com.bladejs.chess.handlers

import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.pieces.Piece
import com.bladejs.chess.misc.Move
import com.bladejs.chess.misc.Position
import com.bladejs.chess.misc.clone
import com.badlogic.gdx.utils.Array

object MoveHandler {
    private val moves = Array<Move>()
    private val currentMoveTypes = Array<Move.Type>()
    private val currentPieces = Array<Piece>()
    private val currentPositions = Array<Position>()

    internal fun reset() {
        moves.clear()
        currentMoveTypes.clear()
        currentPieces.clear()
        currentPositions.clear()
    }

    internal fun appendToMove(moveType: Move.Type, position: Position, piece: Piece) {
        currentMoveTypes.add(moveType)
        currentPositions.add(position)
        currentPieces.add(piece)
    }

    private fun deleteMove() {
        currentPositions.clear()
        currentMoveTypes.clear()
        currentPieces.clear()
    }

    internal fun confirmMove() {
        moves.add(Move(currentMoveTypes.clone(), currentPositions.clone(), currentPieces.clone()))
        deleteMove()
        GameHandler.changeCurrentPlayer()
    }

    internal fun undoMove() {
        if (moves.size == 0) return
        val lastMove = moves[moves.size - 1]
        for (i in lastMove.types.size - 1 downTo 0) {
            if (lastMove.types[i] == Move.Type.ADD) GameBoard.remove(GameBoard[lastMove.positions[i].x][lastMove.positions[i].y].piece!!)
            if (lastMove.types[i] == Move.Type.REMOVE) GameBoard.add(lastMove.pieces[i])
        }
        moves.removeIndex(moves.size - 1)
        deleteMove()
        GameHandler.changeCurrentPlayer()
    }
}