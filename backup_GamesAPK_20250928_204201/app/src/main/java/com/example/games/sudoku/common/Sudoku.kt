package com.example.games.sudoku.common

import kotlin.random.Random
import kotlin.math.sqrt

class Sudoku {

    private var listener: SudokuListener? = null

    var board: Array<IntArray> = arrayOf()
    var solvedBoard: Array<IntArray> = arrayOf()

    private var numRowOrColumn: Int = 0
    private var srNumRowOrColumn: Int = 0
    private var numMissingDigits: Int = 0

    fun init(n: Int, k: Int) {
        numRowOrColumn = n
        numMissingDigits = k
        srNumRowOrColumn = sqrt(n.toDouble()).toInt()
        board = Array(n) { IntArray(n) }
    }

    fun printBoard(): Array<IntArray> {
        llenarValores()
        return board
    }

    fun setListener(l: SudokuListener) {
        listener = l
    }

    private fun noUsadoEnColumna(j: Int, num: Int): Boolean {
        for (i in 0 until numRowOrColumn) {
            if (board[i][j] == num) return false
        }
        return true
    }

    private fun noUsadoEnFila(i: Int, num: Int): Boolean {
        for (j in 0 until numRowOrColumn) {
            if (board[i][j] == num) return false
        }
        return true
    }

    private fun noUsadoEnCaja(filaInicio: Int, columnaInicio: Int, num: Int): Boolean {
        for (i in 0 until srNumRowOrColumn) {
            for (j in 0 until srNumRowOrColumn) {
                if (board[filaInicio + i][columnaInicio + j] == num) return false
            }
        }
        return true
    }

    private fun esSeguro(i: Int, j: Int, num: Int): Boolean {
        val filaInicio = i - i % srNumRowOrColumn
        val columnaInicio = j - j % srNumRowOrColumn
        return noUsadoEnFila(i, num) && noUsadoEnColumna(j, num) && noUsadoEnCaja(filaInicio, columnaInicio, num)
    }

    private fun llenarValores() {
        // reset board
        for (i in 0 until numRowOrColumn) {
            for (j in 0 until numRowOrColumn) {
                board[i][j] = 0
            }
        }

        llenarDiagonal()
        llenarRestantes(0, srNumRowOrColumn)

        // deep-ish copy: clonar cada fila
        solvedBoard = Array(numRowOrColumn) { i -> board[i].clone() }
        listener?.onSolvedBoardCreated(solvedBoard)

        removerKDigitos()
    }

    private fun llenarCaja(fila: Int, columna: Int) {
        for (i in 0 until srNumRowOrColumn) {
            for (j in 0 until srNumRowOrColumn) {
                var num: Int
                do {
                    num = Random.nextInt(1, numRowOrColumn + 1)
                } while (!noUsadoEnCaja(fila, columna, num))
                board[fila + i][columna + j] = num
            }
        }
    }

    private fun llenarDiagonal() {
        var i = 0
        while (i < numRowOrColumn) {
            llenarCaja(i, i)
            i += srNumRowOrColumn
        }
    }

    private fun llenarRestantes(i: Int, j: Int): Boolean {
        var filaActual = i
        var columnaActual = j

        if (columnaActual >= numRowOrColumn && filaActual < numRowOrColumn - 1) {
            filaActual += 1
            columnaActual = 0
        }

        if (filaActual >= numRowOrColumn && columnaActual >= numRowOrColumn) return true

        when {
            filaActual < srNumRowOrColumn -> {
                if (columnaActual < srNumRowOrColumn) columnaActual = srNumRowOrColumn
            }
            filaActual < numRowOrColumn - srNumRowOrColumn -> {
                if (columnaActual == filaActual / srNumRowOrColumn * srNumRowOrColumn) {
                    columnaActual += srNumRowOrColumn
                }
            }
            else -> {
                if (columnaActual == numRowOrColumn - srNumRowOrColumn) {
                    filaActual += 1
                    columnaActual = 0
                    if (filaActual >= numRowOrColumn) return true
                }
            }
        }

        for (num in 1..numRowOrColumn) {
            if (esSeguro(filaActual, columnaActual, num)) {
                board[filaActual][columnaActual] = num
                if (llenarRestantes(filaActual, columnaActual + 1)) return true
                board[filaActual][columnaActual] = 0
            }
        }
        return false
    }

    private fun removerKDigitos() {
        var contador = numMissingDigits
        val total = numRowOrColumn * numRowOrColumn
        while (contador > 0) {
            val idCelda = Random.nextInt(total) // 0..total-1
            val i = idCelda / numRowOrColumn
            val j = idCelda % numRowOrColumn

            if (board[i][j] != 0) {
                board[i][j] = 0
                contador--
            }
        }
    }

    interface SudokuListener {
        fun onSolvedBoardCreated(board: Array<IntArray>)
    }

    companion object {
        val testBoard = arrayOf(
            intArrayOf(4, 0, 1, 2, 3, 6, 8, 9, 7),
            intArrayOf(9, 2, 6, 1, 7, 8, 3, 4, 5),
            intArrayOf(3, 7, 8, 5, 4, 9, 1, 2, 6),
            intArrayOf(1, 6, 7, 4, 5, 3, 9, 8, 2),
            intArrayOf(2, 3, 9, 8, 1, 7, 6, 5, 4),
            intArrayOf(8, 4, 5, 6, 9, 2, 7, 1, 3),
            intArrayOf(6, 8, 3, 9, 2, 4, 5, 7, 1),
            intArrayOf(5, 9, 2, 7, 6, 1, 4, 3, 8),
            intArrayOf(7, 1, 4, 3, 8, 5, 2, 6, 9),
        )
    }
}
