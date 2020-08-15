package com.bladejs.chess

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.bladejs.chess.handlers.InputHandler
import com.bladejs.chess.screens.GameScreen

object ChessGame : Game() {
    lateinit var renderer: ShapeRenderer
    lateinit var batch: SpriteBatch
    lateinit var font: BitmapFont
    lateinit var camera: OrthographicCamera
    var currentScreen: Screen
        get() = getScreen()
        set(value) = setScreen(value)

    override fun create() {
        camera = OrthographicCamera()
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        renderer = ShapeRenderer()
        renderer.setAutoShapeType(true)
        batch = SpriteBatch()
        font = BitmapFont()
        currentScreen = GameScreen(camera)
        Gdx.input.inputProcessor = InputHandler
    }

    override fun dispose() {
        currentScreen.dispose()
        renderer.dispose()
    }
}