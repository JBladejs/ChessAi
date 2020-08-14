package com.bladejs.chess.misc
import com.badlogic.gdx.utils.Array as GdxArray

fun <T> GdxArray<T>.remove(element: T) = removeValue(element, true)

fun <T> GdxArray<T>.clone(): GdxArray<T> {
    val clonedArray = GdxArray<T>()
    clonedArray.addAll(this)
    return clonedArray
}