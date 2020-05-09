package com.bladejs.chess.screens

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.bladejs.chess.entities.Board

class GameScreen : Screen {
    private val camera = OrthographicCamera()
    private val board = Board()

    override fun render(delta: Float) {
        TODO("Not yet implemented")
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }

    override fun resume() {}
    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun hide() {}
    override fun show() {}
}