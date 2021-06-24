package com.bladejs.chess.ai.moves

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ArrayMap
import com.bladejs.chess.ai.data.MoveNode

class MoveSorter {
    //TODO: reduce the number of ArrayMaps
    private val map = ArrayMap<String, ArrayMap<MoveNode, Int>>()

    fun sort(moves: Array<MoveNode>, fen: String, max: Boolean = false) {
        if (map[fen] != null) {
            val moveMap = map[fen]
//            map.clear()
            moves.sort { move1, move2 ->
                val move1val = moveMap[move1]
                val move2val = moveMap[move2]
                val compVal = when {
                    move2val == null -> 1
                    move1val == null -> -1
                    else -> move1val.compareTo(move2val)
                }
                if (max) -compVal else compVal
            }
        }
    }

    fun add(move: MoveNode, value: Int, fen: String) {
        if (map[fen] == null) map.put(fen, ArrayMap())
        map[fen].put(move, value)
    }
}