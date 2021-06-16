package com.bladejs.chess.handlers

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Array
import com.bladejs.chess.ai.AiPlayer
import com.bladejs.chess.ai.board.PieceEvaluator
import com.bladejs.chess.ai.board.PositionEvaluator
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.pieces.*
import com.bladejs.chess.entities.windows.GameOverWindow
import com.bladejs.chess.misc.GameState
import com.bladejs.chess.misc.Move
import com.bladejs.chess.misc.Position
import kotlin.math.abs

//TODO: add board generation here
object GameHandler {
    var aiEnabled = true
    var currentPlayer = Piece.Color.WHITE
    var aiMoving = false
    var aiTurn = false
    var aiCounter = 0

    internal fun changeCurrentPlayer() {
        currentPlayer = if (currentPlayer == Piece.Color.WHITE) Piece.Color.BLACK else Piece.Color.WHITE
    }

    private fun aiMove() {
        if (!aiMoving) {
            var time = System.nanoTime()
            aiMoving = true
            AiPlayer.move()
            aiMoving = false
            time -= System.nanoTime()
            time = -time
            val seconds = time.toFloat() * 0.000000001f
            println(seconds)
        }
    }

    fun update() {
       //TODO: try to find an alternative fix
        if (aiTurn) {
            if (aiCounter > 0) {
                aiMove()
                aiTurn = false
                aiCounter = 0
            } else aiCounter++
        }
    }

    fun addPiece(piece: Piece) {
        GameBoard.add(piece)
        MoveHandler.appendToMove(Move.Type.ADD, Position(piece.x, piece.y), piece.clone())
    }

    fun removePiece(piece: Piece) {
        GameBoard.remove(piece)
        MoveHandler.appendToMove(Move.Type.REMOVE, Position(piece.x, piece.y), piece.clone())
    }

    fun endMove() {
        MoveHandler.confirmMove()
        changeCurrentPlayer()
        generateAvailableMoves()
        if (aiEnabled && currentPlayer == Piece.Color.BLACK) /*aiMove()*/ aiTurn = true
    }

    fun undo(foresight: Boolean = true) {
        if (MoveHandler.undoMove()) changeCurrentPlayer()
        if (!foresight) generateAvailableMoves()
    }

    private fun forceMove(piece: Piece, x: Int, y: Int) {
        val takenPiece = GameBoard[x][y].piece
        if (takenPiece != null) removePiece(takenPiece)
        removePiece(piece)
        piece.x = x
        piece.y = y
        addPiece(piece)
        piece.moveCount++
    }

    fun move(piece: Piece, x: Int, y: Int, foresight: Boolean = true, list: Boolean = false) {
        if (piece.canMoveTo(x, y, list, foresight)) {
            if (piece is Pawn) {
                //Pawn moving two fields
                if (abs(y - piece.y) > 1) piece.movedTwoFields = true
                //En Passant
                if (x != piece.x && GameBoard[x][y].isEmpty) if (piece.color == Piece.Color.BLACK)
                    removePiece(GameBoard[x][y + 1].piece!!) else removePiece(GameBoard[x][y - 1].piece!!)
                if (foresight && (y == 0 || y == 7)) {
                    removePiece(piece)
//                    if (!GameBoard[x][y].isEmpty) GameBoard.remove(GameBoard[x][y].piece!!)
                    if (!GameBoard[x][y].isEmpty) removePiece(GameBoard[x][y].piece!!)
//                    TODO: try to re-add promotion window
//                    GameBoard.promotionWindow.open(Position(x, y))
//                    if (currentPlayer == Piece.Color.WHITE || !aiEnabled)
//                        GameBoard.promotionWindow.open(Position(x, y))
//                    else addPiece(Queen(x, y, Piece.Color.BLACK))
                    addPiece(Queen(x, y, currentPlayer))
                    MoveHandler.confirmMove()
                    changeCurrentPlayer()
                    if (foresight) {
                        checkForMate()
                        generateAvailableMoves()
                        if (aiEnabled && currentPlayer == Piece.Color.BLACK) /*aiMove()*/ aiTurn = true
                    }
//                    MoveHandler.confirmMove()
//                    changeCurrentPlayer()
//                    if (foresight) {
//                        checkForMate()
//                        generateAvailableMoves()
//                        if (aiEnabled && currentPlayer == Piece.Color.BLACK) aiMove()
//                    }
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
            changeCurrentPlayer()
            if (foresight) {
                checkForMate()
                generateAvailableMoves()
                if (aiEnabled && currentPlayer == Piece.Color.BLACK) /*aiMove()*/ aiTurn = true
            }
        }
    }

    fun move(startX: Int, startY: Int, endX: Int, endY: Int, foresight: Boolean = true, list: Boolean = false) {
        if (GameBoard[startX][startY].isEmpty) return
        move(GameBoard[startX][startY].piece!!, endX, endY, foresight, list)
    }

    fun checkForCheck(color: Piece.Color): Boolean {
        val king = GameBoard.pieces.getKing(color)
        //move to another function
        GameBoard.pieces.getPieces(if (color == Piece.Color.WHITE) Piece.Color.BLACK else Piece.Color.WHITE).forEach {
            if (it.canMoveTo(king.x, king.y, foresight = false, ignoreTurn = true)) return true
        }
        return false
    }

    //TODO: find all the places where i iteratively change an array im working on and find a solution to fix them
    fun checkForMate(planning: Boolean = false): GameState {
        if (!aiMoving || planning) {
            val pieceSet = Array<Piece>()
            pieceSet.addAll(GameBoard.pieces.getPieces(currentPlayer))
            pieceSet.forEach {
                if (it.getAvailableMoves().size > 0) return GameState.ONGOING
            }
            return if (checkForCheck(currentPlayer)) {
                if (currentPlayer == Piece.Color.WHITE) {
                    if (!planning) GameBoard.gameOverWindow = GameOverWindow(Gdx.graphics.height * 0.11111f, GameOverWindow.State.LOOSE)
                    GameState.BLACK_WON
                } else {
                    if(!planning) GameBoard.gameOverWindow = GameOverWindow(Gdx.graphics.height * 0.11111f, GameOverWindow.State.WIN)
                    GameState.WHITE_WON
                }
            } else {
                if (!planning) GameBoard.gameOverWindow = GameOverWindow(Gdx.graphics.height * 0.11111f, GameOverWindow.State.DRAW)
                GameState.DRAW
            }
        }
        else return GameState.ONGOING
    }

    fun generateAvailableMoves() {
//        GameBoard.pieces.forEach {
//            it.generateAvailableMoves()
//            println("${it.color} ${it.javaClass.simpleName} ${it.x}-${it.y}")
//        }
        val pieces = Array<Piece>()
        pieces.addAll(GameBoard.pieces.getPieces(Piece.Color.WHITE))
        pieces.addAll(GameBoard.pieces.getPieces(Piece.Color.BLACK))
        for (i in 0 until pieces.size) {
            pieces[i].generateAvailableMoves()
        }
    }
}