package com.bladejs.chess.entities.pieces

import com.badlogic.gdx.graphics.Texture

class Pawn(x: Int, y: Int, color: Color) : Piece(Texture("pawn.png"), Texture("pawnB.png"), x, y, color)