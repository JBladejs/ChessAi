package com.bladejs.chess.entities.windows

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.bladejs.chess.ChessGame

abstract class Window(private val cellSize: Float) {
    open fun render() {
        with(ChessGame.renderer) {
            begin(ShapeRenderer.ShapeType.Filled)
            color.set(Color.BLUE)
            rect((Gdx.graphics.width / 2f) - (2f * cellSize) - 5f, (Gdx.graphics.height / 2f) - (cellSize / 2f) - 5f, cellSize * 4f + 10f, cellSize + 10f)
            end()
        }
    }
}