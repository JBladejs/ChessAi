package com.bladejs.chess.misc

import com.badlogic.gdx.utils.ArrayMap

class PositionMap {
    //TODO: add first position here
    val map = ArrayMap<String, Int>()

    fun addToPosition(fen: String) {
        if (map[fen] == null) map.put(fen, 1)
        else map.put(fen, map[fen] + 1)
    }

    fun removeFromPosition(fen: String) {
        if (map[fen] != null) map.put(fen, map[fen] - 1)
        if (map[fen] <= 0) map.removeKey(fen)
    }

    fun checkPosition(fen: String): Int {
        return map[fen, 0]
    }

}