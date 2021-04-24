package com.bladejs.chess.handlers

import com.badlogic.gdx.Gdx
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.pieces.King
import com.bladejs.chess.entities.pieces.Pawn
import com.bladejs.chess.entities.pieces.Piece
import com.bladejs.chess.entities.windows.GameOverWindow
import com.bladejs.chess.misc.Move
import com.bladejs.chess.misc.Position
import kotlin.math.abs

//TODO: add board generation here
object GameHandler {
    var currentPlayer = Piece.Color.WHITE

    internal fun changeCurrentPlayer() {
        currentPlayer = if (currentPlayer == Piece.Color.WHITE) Piece.Color.BLACK else Piece.Color.WHITE
    }

    fun addPiece(piece: Piece) {
        GameBoard.add(piece)
        MoveHandler.appendToMove(Move.Type.ADD, Position(piece.x, piece.y), piece.clone())
    }

    fun removePiece(piece: Piece) {
        GameBoard.remove(piece)
        MoveHandler.appendToMove(Move.Type.REMOVE, Position(piece.x, piece.y), piece.clone())
    }

    fun endMove() = MoveHandler.confirmMove()

    private fun forceMove(piece: Piece, x: Int, y: Int) {
        val takenPiece = GameBoard[x][y].piece
        if (takenPiece != null) removePiece(takenPiece)
        removePiece(piece)
        piece.x = x
        piece.y = y
        addPiece(piece)
        piece.moveCount++
    }

    fun move(piece: Piece, x: Int, y: Int, foresight: Boolean = true) {
        if (piece.canMoveTo(x, y, foresight)) {
            if (piece is Pawn) {
                //Pawn moving two fields
                if (abs(y - piece.y) > 1) piece.movedTwoFields = true
                //En Passant
                if (x != piece.x && GameBoard[x][y].isEmpty) if (piece.color == Piece.Color.BLACK)
                    removePiece(GameBoard[x][y + 1].piece!!) else removePiece(GameBoard[x][y - 1].piece!!)
                if (foresight && (y == 0 || y == 7)) {
                    removePiece(piece)
                    if (!GameBoard[x][y].isEmpty) GameBoard.remove(GameBoard[x][y].piece!!)
                    GameBoard.promotionWindow.open(Position(x, y))
                    return
                }
            }
            //Castling
            if (piece is King && abs(x - piece.x) > 1) {
                if (x == 2) forceMove(GameBoard[0][y].piece!!, 3, y)
                else forceMove(GameBoard[7][y].piece!!, 5, y)
            }
            //Normal move
            forceMove(piece, x, y)
            MoveHandler.confirmMove()
            if (foresight) checkForMate()
        }
    }

    fun move(startX: Int, startY: Int, endX: Int, endY: Int, foresight: Boolean = true) {
        if (GameBoard[startX][startY].isEmpty) return
        move(GameBoard[startX][startY].piece!!, endX, endY, foresight)
    }

    fun checkForCheck(color: Piece.Color): Boolean {
        val king = GameBoard.pieces.getKing(color)
        //move to another function
        GameBoard.pieces.getPieces(if (color == Piece.Color.WHITE) Piece.Color.BLACK else Piece.Color.WHITE).forEach {
            if (it.canMoveTo(king.x, king.y, foresight = false, ignoreTurn = true)) return true
        }
        return false
    }

    fun checkForMate() {
        val pieceSet = GameBoard.pieces.getPieces(currentPlayer)
        pieceSet.forEach {
            if (it.getAvailableMoves().size > 0) return
        }
        GameBoard.gameOverWindow = if (checkForCheck(currentPlayer))
            GameOverWindow(Gdx.graphics.height * 0.11111f, if (currentPlayer == Piece.Color.WHITE) GameOverWindow.State.LOOSE else GameOverWindow.State.WIN)
        else
            GameOverWindow(Gdx.graphics.height * 0.11111f, GameOverWindow.State.DRAW)
    }

    fun undo() = MoveHandler.undoMove()
}