package com.bladejs.chess.entities.windows

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.bladejs.chess.ChessGame

//TODO: fix text location
//TODO: fix flashing (make window appear after rendering the whole move)
class GameOverWindow(private val size: Float, private val state: State) : Window(size) {
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
        with(ChessGame.batch) {
            begin()
            font.draw(ChessGame.batch, endText, (Gdx.graphics.width / 2f) - (2f * size) - 5f, (Gdx.graphics.height / 2f) - (size / 2f) - 5f)
            end()
        }
    }
}