package com.bladejs.chess.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.bladejs.chess.ChessGame
import com.bladejs.chess.ai.board.PieceEvaluator
import com.bladejs.chess.ai.board.PositionEvaluator
import com.bladejs.chess.entities.pieces.*
import com.bladejs.chess.entities.pieces.Piece.Color.BLACK
import com.bladejs.chess.entities.pieces.Piece.Color.WHITE
import com.bladejs.chess.entities.windows.GameOverWindow
import com.bladejs.chess.entities.windows.PromotionWindow
import com.bladejs.chess.handlers.GameHandler
import com.bladejs.chess.misc.Position
import com.badlogic.gdx.utils.Array as GdxArray

//TODO: change to class
object GameBoard {
    private val board = GdxArray<GdxArray<BoardField>>(8)
    val pieces = PieceCollection()
    private var cellSize = Gdx.graphics.height * 0.111111f // height / 9
    private var invCellSize = 9f / Gdx.graphics.height // cell size ^ -1
    private var halfCellSize = cellSize * 0.5f // cell size / 2
    private var doubleCellSize = cellSize * 2f // cell size * 2
    private var quarterCellSize = halfCellSize * 0.5f // cell size / 4
    val promotionWindow = PromotionWindow(cellSize)
    var gameOverWindow: GameOverWindow? = null
    var rendering = true

    fun reset() {
        for (i in 0..7) {
            board.add(GdxArray(8))
            for (j in 0..7) {
                board[i].add(BoardField())
            }
        }
        //TODO: implement FEN notation
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

//        debug:
//        add(Rook(0, 0, WHITE))
//        add(Bishop(2, 0, WHITE))
//        add(Queen(3, 0, WHITE))
//        add(King(4, 0, WHITE))
//        add(Bishop(5, 0, WHITE))
//        add(Rook(7, 0, WHITE))
//        add(Pawn(3, 1, WHITE))
//        add(Pawn(5, 1, WHITE))
//        add(Pawn(6, 1, WHITE))
//        add(Pawn(4, 2, WHITE))
//        add(Knight(5, 2, WHITE))
//        add(Pawn(6, 2, WHITE))
//        add(Queen(1, 3, BLACK))
//        add(Pawn(2, 3, WHITE))
//        add(Knight(4, 3, WHITE))
//        add(Bishop(6, 3, BLACK))
//        add(Rook(0, 5, BLACK))
//        val king = King(1, 5, BLACK)
//        king.moveCount++
//        add(king)
//        add(Pawn(3, 5, BLACK))
//        add(Pawn(1, 6, BLACK))
//        add(Pawn(2, 6, BLACK))
//        add(Pawn(4, 6, BLACK))
//        add(Pawn(5, 6, BLACK))
//        add(Pawn(6, 6, BLACK))
//        add(Pawn(7, 6, BLACK))
//        add(Bishop(5, 7, BLACK))
//        add(Rook(7, 7, BLACK))

        GameHandler.generateAvailableMoves()
    }

    init {
        reset()
    }

    operator fun get(i: Int): GdxArray<BoardField> = board[i]

    operator fun get(i: Int, j: Int): BoardField = board[i][j]

    operator fun set(i: Int, j: Int, piece: Piece?) {
        val oldPiece = board[i][j].piece
        if (oldPiece != null) remove(oldPiece)
        if (piece != null) add(piece)
    }

    fun getFieldAt(x: Float, y: Float): Position {
        if (x < halfCellSize || y < halfCellSize) return Position(-1, -1)
        //division = multiplication by inversion
        val i = ((x - halfCellSize) * invCellSize).toInt()
        val j = ((y - halfCellSize) * invCellSize).toInt()
        return Position(i, j)
    }

    fun add(piece: Piece) {
//        if (piece is Rook) throw Exception()
        pieces.add(piece)
        board[piece.x][piece.y].piece = piece
    }

    fun remove(piece: Piece) {
        pieces.remove(piece)
        board[piece.x][piece.y].piece = null
    }

    fun highlight(fields: GdxArray<Position>) {
        fields.forEach {
            board[it.x][it.y].isHighlighted = true
        }
    }

    fun deHighlight() {
        board.forEach { row ->
            row.forEach {
                it.isHighlighted = false
            }
        }
    }

    fun render() {
//        cellSize = Gdx.graphics.height * 0.111111f
//        invCellSize = 9f / Gdx.graphics.height
//        halfCellSize = cellSize * 0.5f
//        doubleCellSize = cellSize * 2f
//        quarterCellSize = halfCellSize * 0.5f
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
            color.set(0f, 0f, 1f, 0.5f)
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
            pieces.forEach {
                it.render(cellSize, halfCellSize)
            }
            end()
        }
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        with(ChessGame.renderer) {
            begin(ShapeRenderer.ShapeType.Filled)
            color.set(0f, 0f, 1f, 0.5f)
            for (i in 0..7) {
                for (j in 0..7) {
                    if (board[i][j].isHighlighted) rect((halfCellSize + i * cellSize), (halfCellSize + j * cellSize), cellSize, cellSize)
                }
            }
            end()
        }
        Gdx.gl.glDisable(GL20.GL_BLEND)
    }
}
