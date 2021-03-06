package com.bladejs.chess.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.bladejs.chess.ChessGame

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        with(config) {
            title = "Chess"
            width = 600
            height = 600
            resizable = false
            LwjglApplication(ChessGame, this)
        }
    }
}