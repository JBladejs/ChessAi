package com.bladejs.chess

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.bladejs.chess.screens.GameScreen

object ChessGame : Game() {
    lateinit var renderer: ShapeRenderer
    lateinit var batch: SpriteBatch
    lateinit var font: BitmapFont
    var currentScreen: Screen
    get() = getScreen()
    set(value) = setScreen(value)

    override fun create() {
        renderer = ShapeRenderer()
        renderer.setAutoShapeType(true)
        batch = SpriteBatch()
        font = BitmapFont()
        currentScreen = GameScreen()
    }

    override fun dispose() {
        currentScreen.dispose()
        renderer.dispose()
    }
}