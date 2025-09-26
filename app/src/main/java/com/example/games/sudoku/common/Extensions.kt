package com.example.games.sudoku.common

fun <T> MutableList<T>.setOrAdd(index: Int, value: T) {
    if (index < size) {
        this[index] = value
    } else {
        add(value)
    }
}

fun List<Cell>.getCellById(id: Int): Cell? {
    return this.flatMap { listOf(it) + it.subCells }.find { it.id == id }
}