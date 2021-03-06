package com.bladejs.chess.entities.windows

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import com.bladejs.chess.ChessGame
import com.bladejs.chess.entities.pieces.*
import com.bladejs.chess.handlers.GameHandler
import com.bladejs.chess.misc.Position

class PromotionWindow(size: Float): Window(size) {
    private val promotionOptions = Array<Rectangle>(4)
    var promotion = false
    var promotionPosition = Position(0, 0)

    init {
        promotionOptions.add(Rectangle((Gdx.graphics.width / 2f) - (2f * size), (Gdx.graphics.height / 2f) - (size / 2f), size, size))
        promotionOptions.add(Rectangle((Gdx.graphics.width / 2f) - size, (Gdx.graphics.height / 2f) - (size / 2f), size, size))
        promotionOptions.add(Rectangle((Gdx.graphics.width / 2f), (Gdx.graphics.height / 2f) - (size / 2f), size, size))
        promotionOptions.add(Rectangle((Gdx.graphics.width / 2) + size, (Gdx.graphics.height / 2) - (size / 2f), size, size))
    }

    private enum class PromotionPiece {
        QUEEN, KNIGHT, ROOK, BISHOP
    }

    fun open(promotionPosition: Position) {
        this.promotionPosition = promotionPosition
        promotion = true
    }

    private fun promote(piece: PromotionPiece) {
        val color = if (promotionPosition.y == 7) Piece.Color.WHITE else Piece.Color.BLACK
        when (piece) {
            PromotionPiece.QUEEN -> GameHandler.addPiece(Queen(promotionPosition.x, promotionPosition.y, color))
            PromotionPiece.KNIGHT -> GameHandler.addPiece(Knight(promotionPosition.x, promotionPosition.y, color))
            PromotionPiece.ROOK -> GameHandler.addPiece(Rook(promotionPosition.x, promotionPosition.y, color))
            PromotionPiece.BISHOP -> GameHandler.addPiece(Bishop(promotionPosition.x, promotionPosition.y, color))
        }
        promotion = false
        GameHandler.endMove()
    }

    fun handlePromotionMenuInput(mouseX: Float, mouseY: Float) {
        var clickedOnPiece = false
        var j = 0
        for (i in 0 until promotionOptions.size) {
            if (promotionOptions[i].contains(mouseX, mouseY)) {
                clickedOnPiece = true
                j = i
                break
            }
        }
        if (clickedOnPiece) when (j) {
            0 -> promote(PromotionPiece.QUEEN)
            1 -> promote(PromotionPiece.KNIGHT)
            2 -> promote(PromotionPiece.ROOK)
            3 -> promote(PromotionPiece.BISHOP)
        }
    }

    override fun render() {
        super.render()
        with(ChessGame.batch) {
            begin()
            val color = if (promotionPosition.y == 7) Piece.Color.WHITE else Piece.Color.BLACK
            Queen(0, 0, color).renderPrecise(promotionOptions[0].x, promotionOptions[0].y, promotionOptions[0].width)
            Knight(0, 0, color).renderPrecise(promotionOptions[1].x, promotionOptions[1].y, promotionOptions[1].width)
            Rook(0, 0, color).renderPrecise(promotionOptions[2].x, promotionOptions[2].y, promotionOptions[2].width)
            Bishop(0, 0, color).renderPrecise(promotionOptions[3].x, promotionOptions[3].y, promotionOptions[3].width)
            end()
        }
    }
}