package com.example.games.sudoku.common

data class Cell(    // ← Nombre ORIGINAL
    val id: Int = generateId(),
    val n: Int = 0,
    val parentN: Int = 0,
    val parentId: Int = 0,
    val missingNum: Boolean = false,    // ← Original
    val subCells: List<Cell> = emptyList()  // ← Original
) {
    companion object {
        private var idCounter = 0
        fun generateId(): Int {
            return idCounter++
        }
    }
}