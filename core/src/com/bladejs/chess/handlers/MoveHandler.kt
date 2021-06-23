package com.bladejs.chess.handlers

import com.badlogic.gdx.Game
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

    internal fun confirmMove(includeFEN: Boolean = true) {
        val fen = if (includeFEN) {
            GameBoard.getFEN()
        }
        else "l"
        if (includeFEN) GameHandler.positionMap.addToPosition(fen)
        moves.add(Move(currentMoveTypes.clone(), currentPositions.clone(), currentPieces.clone(), GameBoard.fieldFrom, GameBoard.fieldTo, fen))
        deleteMove()
    }

    internal fun undoMove(): Boolean {
        if (moves.size == 0) return false
        val lastMove = moves[moves.size - 1]
        for (i in lastMove.types.size - 1 downTo 0) {
            if (lastMove.types[i] == Move.Type.ADD) GameBoard.remove(GameBoard[lastMove.positions[i].x][lastMove.positions[i].y].piece!!)
            if (lastMove.types[i] == Move.Type.REMOVE) GameBoard.add(lastMove.pieces[i])
        }
        GameBoard.fieldFrom = lastMove.fieldFrom
        GameBoard.fieldTo = lastMove.fieldTo
        if (lastMove.fen != "l") GameHandler.positionMap.removeFromPosition(lastMove.fen)
        moves.removeIndex(moves.size - 1)
        deleteMove()
        return true
    }
}