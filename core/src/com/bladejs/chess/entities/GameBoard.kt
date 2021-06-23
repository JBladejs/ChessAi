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
    var fieldFrom: Pair<Int, Int>? = null
    var fieldTo: Pair<Int, Int>? = null
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
//        add(King(0, 6, WHITE))
//        add(King(7, 7, BLACK))
//        add(Bishop(3, 6, BLACK))
//        add(Queen(1, 4, BLACK))
//        add(Pawn(5, 2, BLACK))
//        add(Pawn(6, 1, BLACK))
//        add(Pawn(7, 2, BLACK))
//        add(Queen(3, 0, BLACK))

//        add(Pawn(0, 1, WHITE))
//        add(King(4, 0, WHITE))
//        add(Rook(0, 0, WHITE))
//        add(Rook(7, 0, WHITE))
//        add(King(4, 7, BLACK))
//        add(Queen(3, 7, BLACK))

        GameHandler.generateAvailableMoves()
    }

    init {
        reset()
    }

    fun getFEN(): String {
        var fen = ""
        var emptyIndex = 0
        for (i in 7 downTo 0) {
            for (j in 0..7) {
                val piece = board[j][i].piece
                if (piece == null) emptyIndex++
                else {
                    if (emptyIndex > 0) {
                        fen += emptyIndex.toString()
                        emptyIndex = 0
                    }
                    var char = when(piece) {
                        is Bishop -> 'B'
                        is King -> 'K'
                        is Knight -> 'N'
                        is Pawn -> 'P'
                        is Queen -> 'Q'
                        is Rook -> 'R'
                        else -> 'E'
                    }
                    if (piece.color == BLACK) char = char.toLowerCase()
                    fen += char
                }
            }
            if (emptyIndex > 0) {
                fen += emptyIndex.toString()
                emptyIndex = 0

            }
            fen += '/'
        }
        fen += if (GameHandler.currentPlayer == WHITE) 'w' else 'b'
        return fen
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
            if (fieldFrom != null && fieldTo != null) {
                color.set(1f, 1f, 0f, 0.5f)
                rect((halfCellSize + fieldFrom!!.first * cellSize), (halfCellSize + fieldFrom!!.second * cellSize), cellSize, cellSize)
                rect((halfCellSize + fieldTo!!.first * cellSize), (halfCellSize + fieldTo!!.second * cellSize), cellSize, cellSize)
            }
            end()
        }
        Gdx.gl.glDisable(GL20.GL_BLEND)
    }
}
