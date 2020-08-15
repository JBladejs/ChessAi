package com.bladejs.chess.handlers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Vector3
import com.bladejs.chess.ChessGame
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.pieces.Piece
import com.badlogic.gdx.Input.Keys

object InputHandler : InputProcessor {
    private val mousePos = Vector3()
    private var startingMousePos = Vector3()
    private val camera = ChessGame.camera
    private var draggedPiece: Piece? = null

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false
    override fun keyTyped(character: Char): Boolean = false
    override fun scrolled(amount: Int): Boolean = false
    override fun keyDown(keycode: Int): Boolean = false

    override fun keyUp(keycode: Int): Boolean {
        if (GameBoard.promotion) return false
        if (keycode == Keys.U) GameBoard.undo()
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        camera.unproject(mousePos.set(screenX.toFloat(), screenY.toFloat(), 0f))
        if (!GameBoard.promotion) {
            val position = GameBoard.getFieldAt(mousePos.x, mousePos.y)
            if (position.x < 8 && position.y < 8 && position.x >= 0 && position.y >= 0) {
                val field = GameBoard[position.x][position.y]
                if (!field.isEmpty) draggedPiece = field.piece
                startingMousePos = mousePos.cpy()
            }
        } else {
            GameBoard.handlePromotionMenuInput(mousePos.x, mousePos.y)
        }
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        if (GameBoard.promotion) return false
        val piece = draggedPiece
        camera.unproject(mousePos.set(screenX.toFloat(), screenY.toFloat(), 0f))
        if (piece != null) {
            piece.draggedX = mousePos.x - startingMousePos.x
            piece.draggedY = mousePos.y - startingMousePos.y
        }
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (GameBoard.promotion) return false
        camera.unproject(mousePos.set(screenX.toFloat(), screenY.toFloat(), 0f))
        val piece = draggedPiece
        draggedPiece = null
        if (piece != null) {
            piece.draggedX = 0f
            piece.draggedY = 0f
            val position = GameBoard.getFieldAt(mousePos.x, mousePos.y)
            if (position.x < 8 && position.y < 8 && position.x >= 0 && position.y >= 0)
                GameBoard.move(piece, position.x, position.y)
        }
        return true
    }
}