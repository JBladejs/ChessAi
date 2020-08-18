package com.bladejs.chess.entities.pieces.windows

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.bladejs.chess.ChessGame

class GameOverWindow(private val cellSize: Float, private val state: State) : Window(cellSize) {
    private val font = BitmapFont()

    enum class State {
        WIN, LOOSE, DRAW
    }

    override fun render() {
        super.render()
        font.color = Color.BLACK
        val endText = when (state) {
            State.WIN -> "YOU WIN!"
            State.DRAW -> "DRAW!"
            State.LOOSE -> "YOU LOOSE!"
        }
        font.draw(ChessGame.batch, endText, (Gdx.graphics.width / 2f) - (2f * cellSize) - 5f, (Gdx.graphics.height / 2f) - (cellSize / 2f) - 5f)
    }
}