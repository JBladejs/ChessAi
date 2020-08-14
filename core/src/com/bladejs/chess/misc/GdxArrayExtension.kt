package com.bladejs.chess.misc
import com.badlogic.gdx.utils.Array as GdxArray

fun <T> GdxArray<T>.remove(element: T) = removeValue(element, true)