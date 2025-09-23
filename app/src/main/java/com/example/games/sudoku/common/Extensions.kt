package com.example.games.sudoku.common


// Mapea dificultad -> cantidad de casillas removidas (ajusta números si quieres)
val Difficulty.missingDigits: Int
    get() = when (this) {
        Difficulty.Fast -> 20
        Difficulty.Easy -> 30
        Difficulty.Normal -> 40
        Difficulty.Hard -> 50
        Difficulty.Evil -> 55
    }

// setOrAdd: si el índice existe lo setea, si no lo añade (rellena hasta el índice)
fun <T> MutableList<T>.setOrAdd(index: Int, value: T) {
    if (index in 0 until size) this[index] = value
    else {
        while (size < index) add(value)
        add(value)
    }
}
