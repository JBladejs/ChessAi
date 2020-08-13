package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture

class Queen(x: Int, y: Int, color: Color) : Piece(Texture("queen.png"), Texture("queenB.png"), x, y, color)