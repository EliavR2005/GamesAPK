package com.example.games.sudoku.common

import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class GameEngine(
    private val countUpTimer: CountUpTimer
) {
    private val gson: Gson = Gson()
    private val unre: Unre<List<Cell>> = Unre()
    private val sudoku: Sudoku = Sudoku()
    private val mSolvedBoard: ArrayList<Cell> = arrayListOf()

    // scope propio (se puede cancelar si quieres limpiar)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    // Flows
    private val _solvedBoard = MutableSharedFlow<List<Cell>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val solvedBoard: SharedFlow<List<Cell>> = _solvedBoard

    private val _currentBoard = MutableSharedFlow<List<Cell>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val currentBoard: SharedFlow<List<Cell>> = _currentBoard

    private val _remainingNumbers = MutableSharedFlow<List<RemainingNumber>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val remainingNumbers: SharedFlow<List<RemainingNumber>> = _remainingNumbers

    private val _win = MutableStateFlow(false)
    val win: StateFlow<Boolean> = _win

    val second = countUpTimer.second

    init {
        // Listener de Sudoku
        sudoku.setListener(object : Sudoku.SudokuListener {
            override fun onSolvedBoardCreated(board: Array<IntArray>) {
                val mBoard = toCellBoard(board)
                mSolvedBoard.clear()
                mSolvedBoard.addAll(mBoard)

                scope.launch {
                    _solvedBoard.emit(mBoard)
                }
            }
        })

        // Listener de Unre (undo/redo)
        unre.setListener(object : Unre.UnreListener<List<Cell>> {
            override fun onUndo(data: List<Cell>) {
                scope.launch { _currentBoard.emit(data) }
            }

            override fun onRedo(data: List<Cell>) {
                scope.launch { _currentBoard.emit(data) }
            }
        })
    }

    private fun toCellBoard(board: Array<IntArray>): List<Cell> {
        val mBoard = ArrayList<Cell>()
        val dCells1 = arrayListOf<List<Int>>()
        val dCells2 = arrayListOf<List<Int>>()
        val dCells3 = arrayListOf<List<Int>>()

        for (i in 0 until 9) {
            val cellList = board[i].toList()
            dCells1.add(cellList.subList(0, 3))
            dCells2.add(cellList.subList(3, 6))
            dCells3.add(cellList.subList(6, 9))
        }

        for (i in 0 until 9) {
            val parentId = Random.nextInt()
            val subCells = ArrayList<Cell>()

            val (from, to) = when (i) {
                in 0 until 3 -> 0 to 3
                in 3 until 6 -> 3 to 6
                in 6 until 9 -> 6 to 9
                else -> -1 to -1
            }

            val dCellList = when {
                i % 3 == 0 -> dCells1.subList(from, to)
                i % 3 == 1 -> dCells2.subList(from, to)
                i % 3 == 2 -> dCells3.subList(from, to)
                else -> emptyList()
            }

            dCellList.flatten().forEach { int ->
                subCells.add(
                    Cell(
                        n = int,
                        parentN = i + 1,
                        parentId = parentId,
                        missingNum = int == 0
                    )
                )
            }

            mBoard.add(Cell(n = i + 1, parentId = parentId, subCells = subCells))
        }
        return mBoard
    }

    suspend fun init(difficulty: Difficulty) {
        sudoku.init(9, difficulty.missingDigits)
        val board = sudoku.printBoard()
        val cellBoards = toCellBoard(board)

        _currentBoard.emit(cellBoards)
        unre.swap(cellBoards)
        getRemainingNumber(cellBoards)

        countUpTimer.reset()
        countUpTimer.start()
    }

    suspend fun updateBoard(cell: Cell, num: Int, action: SudokuGameAction) {
        val boardValue = currentBoard.replayCache.lastOrNull() ?: return
        val parentCell = boardValue.getOrNull(cell.parentN - 1) ?: return
        val cellIndex = parentCell.subCells.indexOfFirst { it.id == cell.id }

        if (cellIndex != -1) {
            val updatedCell = when (action) {
                SudokuGameAction.PENCIL -> {
                    cell.copy(n = -1)
                }
                else -> {
                    cell.copy(
                        n = when {
                            cell.n == 0 -> num
                            cell.n != num -> num
                            else -> 0
                        }
                    )
                }
            }

            val newBoard = boardValue.toMutableList().map { parent ->
                if (parent.id == cell.parentId) {
                    parent.copy(subCells = parent.subCells.toMutableList().apply {
                        set(cellIndex, updatedCell)
                    })
                } else {
                    parent
                }
            }

            _currentBoard.emit(newBoard)
            unre.addStack(newBoard)
            _win.emit(checkWin())
        }
    }

    suspend fun getRemainingNumber(board: List<Cell>) {
        val numbers = IntArray(9)

        board.flatMap { it.subCells }.forEach { cell ->
            if (cell.n in 1..9) numbers[cell.n - 1]++
        }

        val newRemainingNumbers = (1..9).map { num ->
            RemainingNumber(n = num, remaining = 9 - numbers[num - 1])
        }

        _remainingNumbers.emit(newRemainingNumbers)
    }

    fun checkWin(): Boolean {
        val board = currentBoard.replayCache.lastOrNull() ?: return false
        return board.flatMap { it.subCells }.all { cell ->
            mSolvedBoard.flatMap { it.subCells }.any { solvedCell ->
                solvedCell.id == cell.id && solvedCell.n == cell.n
            }
        }
    }

    fun undo() {
        unre.undo()
    }

    fun redo() {
        unre.redo()
    }

    suspend fun solve() {
        _currentBoard.emit(mSolvedBoard)
        _win.emit(true)
        pause()
    }

    fun pause() {
        countUpTimer.stop()
    }

    fun resume() {
        countUpTimer.start()
    }

    fun reset() {
        countUpTimer.reset()
    }

    fun clear() {
        scope.cancel()
    }
}
