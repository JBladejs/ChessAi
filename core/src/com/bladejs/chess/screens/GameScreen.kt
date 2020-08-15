package com.bladejs.chess.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.bladejs.chess.ChessGame
import com.bladejs.chess.entities.GameBoard
import com.bladejs.chess.entities.PromotionWindow
import com.bladejs.chess.misc.IntColor

class GameScreen(private val camera: OrthographicCamera) : Screen {
    private val board = GameBoard
    private val background = IntColor(27, 94, 20, 255)

    override fun render(delta: Float) {
        if (!GameBoard.promotionWindow.promotion) {
            Gdx.gl.glClearColor(background.red, background.green, background.blue, background.alpha)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
            camera.update()
            board.render()
        } else {
            camera.update()
            GameBoard.promotionWindow.render()
        }
    }

    override fun dispose() {
    }

    override fun resume() {}
    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun hide() {}
    override fun show() {}
}