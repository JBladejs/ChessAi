package com.bladejs.chess.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array as GdxArray
import com.bladejs.chess.ChessGame
import com.bladejs.chess.entities.pieces.*
import com.bladejs.chess.entities.pieces.Piece.Color.*
import com.bladejs.chess.handlers.GameHandler
import com.bladejs.chess.misc.Move
import com.bladejs.chess.misc.Position
import com.bladejs.chess.misc.remove
import com.bladejs.chess.screens.GameScreen
import kotlin.math.abs

object GameBoard {
    private val board = GdxArray<GdxArray<BoardField>>(8)
    private val pieces = GdxArray<Piece>()
    private var cellSize = Gdx.graphics.height / 9f
    private var halfCellSize = cellSize / 2f
    private var doubleCellSize = cellSize * 2f
    private var quarterCellSize = halfCellSize / 2f
    var promotion = false
    var promotionPosition = Position(0, 0)
    private val promotionOptions = GdxArray<Rectangle>(4)

    enum class PromotionPiece {
        QUEEN, KNIGHT, ROOK, BISHOP
    }

    init {
        for (i in 0..7) {
            board.add(GdxArray<BoardField>(8))
            for (j in 0..7) {
                board[i].add(BoardField())
            }
        }
        for (i in 0..7) add(Pawn(i, 1, WHITE))
        for (i in 0..7) add(Pawn(i, 6, BLACK))
        add(King(4, 0, WHITE))
        add(Queen(3, 0, WHITE))
        add(Rook(0, 0, WHITE))
        add(Rook(7, 0, WHITE))
        add(Bishop(2, 0, WHITE))
        add(Bishop(5, 0, WHITE))
        add(Knight(1, 0, WHITE))
        add(Knight(6, 0, WHITE))
        add(King(4, 7, BLACK))
        add(Queen(3, 7, BLACK))
        add(Rook(0, 7, BLACK))
        add(Rook(7, 7, BLACK))
        add(Bishop(2, 7, BLACK))
        add(Bishop(5, 7, BLACK))
        add(Knight(1, 7, BLACK))
        add(Knight(6, 7, BLACK))
        GameHandler.deleteMove()
        promotionOptions.add(Rectangle((Gdx.graphics.width / 2f) - (2f * cellSize), (Gdx.graphics.height / 2f) - halfCellSize, cellSize, cellSize))
        promotionOptions.add(Rectangle((Gdx.graphics.width / 2f) - cellSize, (Gdx.graphics.height / 2f) - halfCellSize, cellSize, cellSize))
        promotionOptions.add(Rectangle((Gdx.graphics.width / 2f), (Gdx.graphics.height / 2f) - halfCellSize, cellSize, cellSize))
        promotionOptions.add(Rectangle((Gdx.graphics.width / 2) + cellSize, (Gdx.graphics.height / 2) - halfCellSize, cellSize, cellSize))
    }

    operator fun set(i: Int, j: Int, piece: Piece?) {
        val oldPiece = board[i][j].piece
        if (oldPiece != null) remove(oldPiece)
        if (piece != null) add(piece)
    }

    operator fun get(i: Int): GdxArray<BoardField> = board[i]

    fun getFieldAt(x: Float, y: Float): Position {
        if (x < halfCellSize || y < halfCellSize) return Position(-1, -1)
        val i = ((x - halfCellSize) / cellSize).toInt()
        val j = ((y - halfCellSize) / cellSize).toInt()
        return Position(i, j)
    }

    fun add(piece: Piece) {
        pieces.add(piece)
        board[piece.x][piece.y].piece = piece
        GameHandler.appendToMove(Move.Type.ADD, Position(piece.x, piece.y), piece.clone())
    }

    fun remove(piece: Piece) {
        pieces.remove(piece)
        board[piece.x][piece.y].piece = null
        GameHandler.appendToMove(Move.Type.REMOVE, Position(piece.x, piece.y), piece.clone())
    }

    private fun forceMove(piece: Piece, x: Int, y: Int) {
        val takenPiece = board[x][y].piece
        if (takenPiece != null) remove(takenPiece)
        remove(piece)
        piece.x = x
        piece.y = y
        add(piece)
        piece.moveCount++
    }

    fun move(piece: Piece, x: Int, y: Int) {
        if (piece.canMoveTo(x, y)) {
            if (piece is Pawn) {
                //Pawn moving two fields
                if (abs(y - piece.y) > 1) piece.movedTwoFields = true
                //En Passant
                if (x != piece.x && board[x][y].isEmpty) if (piece.color == BLACK)
                    remove(board[x][y + 1].piece!!) else remove(board[x][y - 1].piece!!)
                if (y == 0 || y == 7) {
                    remove(piece)
                    if (!board[x][y].isEmpty) remove(board[x][y].piece!!)
                    promotion = true
                    promotionPosition = Position(x, y)
                    return
                }
            }
            //Castling
            if (piece is King && abs(x - piece.x) > 1) {
                if (x == 2) forceMove(board[0][y].piece!!, 3, y)
                else forceMove(board[7][y].piece!!, 5, y)
            }
            //Normal move
            forceMove(piece, x, y)
            GameHandler.confirmMove()
        }
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

    private fun promote(piece: PromotionPiece) {
        val color = if (promotionPosition.y == 7) WHITE else BLACK
        when (piece) {
            PromotionPiece.QUEEN -> add(Queen(promotionPosition.x, promotionPosition.y, color))
            PromotionPiece.KNIGHT -> add(Knight(promotionPosition.x, promotionPosition.y, color))
            PromotionPiece.ROOK -> add(Rook(promotionPosition.x, promotionPosition.y, color))
            PromotionPiece.BISHOP -> add(Bishop(promotionPosition.x, promotionPosition.y, color))
        }
        promotion = false
        GameHandler.confirmMove()
    }

    fun undo() = GameHandler.undoMove()

    fun render() {
        cellSize = Gdx.graphics.height / 9f
        halfCellSize = cellSize / 2f
        doubleCellSize = cellSize * 2f
        quarterCellSize = halfCellSize / 2f
        with(ChessGame.renderer) {
            begin(ShapeRenderer.ShapeType.Filled)
            color.set(Color.BROWN)
            rect(0f, 0f, halfCellSize, Gdx.graphics.height.toFloat())
            rect(0f, 0f, Gdx.graphics.width.toFloat(), halfCellSize)
            rect(0f, Gdx.graphics.height.toFloat() - halfCellSize, Gdx.graphics.width.toFloat(), halfCellSize)
            rect(Gdx.graphics.width.toFloat() - halfCellSize, 0f, halfCellSize, Gdx.graphics.height.toFloat())
            color.set(Color.WHITE)
            for (i in 0..7) {
                for (j in 0..3) {
                    val start = if (i % 2 == 0) cellSize + halfCellSize else halfCellSize
                    rect(start + (j * doubleCellSize), halfCellSize + (i * cellSize), cellSize, cellSize)
                }
            }
            end()
        }
        with(ChessGame.batch) {
            begin()
            for (i in 0..1) {
                var character = 'a'
                val horizontalPlacement = if (i == 0) quarterCellSize + ChessGame.font.capHeight else Gdx.graphics.height - quarterCellSize
                for (j in 1..8) ChessGame.font.draw(this, (character++).toString(), j * cellSize, horizontalPlacement)
            }
            for (i in 0..1) {
                var character = 1
                val verticalPlacement = if (i == 0) quarterCellSize else Gdx.graphics.height - quarterCellSize
                for (j in 1..8) ChessGame.font.draw(this, (character++).toString(), verticalPlacement, j * cellSize)
            }

            pieces.forEach { it.render(cellSize, halfCellSize) }
            end()
        }
    }

    fun renderPromotion() {
        with(ChessGame.renderer) {
            begin(ShapeRenderer.ShapeType.Filled)
            color.set(Color.BLUE)
            rect((Gdx.graphics.width / 2f) - (2f * cellSize) - 5f, (Gdx.graphics.height / 2f) - halfCellSize - 5f, cellSize * 4f + 10f, cellSize + 10f)
            end()
        }
        with(ChessGame.batch) {
            begin()
            val color = if (promotionPosition.y == 7) WHITE else BLACK
            Queen(0, 0, color).renderPrecise(promotionOptions[0].x, promotionOptions[0].y, promotionOptions[0].width)
            Knight(0, 0, color).renderPrecise(promotionOptions[1].x, promotionOptions[1].y, promotionOptions[1].width)
            Rook(0, 0, color).renderPrecise(promotionOptions[2].x, promotionOptions[2].y, promotionOptions[2].width)
            Bishop(0, 0, color).renderPrecise(promotionOptions[3].x, promotionOptions[3].y, promotionOptions[3].width)
            end()
        }
    }
}
