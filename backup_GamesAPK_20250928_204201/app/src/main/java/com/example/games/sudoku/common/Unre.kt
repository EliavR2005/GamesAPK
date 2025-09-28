package com.example.games.sudoku.common

class Unre<T> {
    private val undoStack = mutableListOf<T>()
    private val redoStack = mutableListOf<T>()
    private var listener: UnreListener<T>? = null

    fun addStack(data: T) {
        undoStack.add(data)
        redoStack.clear()
    }

    fun swap(data: T) {
        undoStack.clear()
        redoStack.clear()
        undoStack.add(data)
    }

    fun undo() {
        if (undoStack.size > 1) {
            val currentData = undoStack.removeAt(undoStack.size - 1)
            redoStack.add(currentData)
            listener?.onUndo(undoStack.last())
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            val data = redoStack.removeAt(redoStack.size - 1)
            undoStack.add(data)
            listener?.onRedo(data)
        }
    }

    fun setListener(l: UnreListener<T>) {
        listener = l
    }

    interface UnreListener<T> {
        fun onUndo(data: T)  // ← En inglés
        fun onRedo(data: T)   // ← En inglés
    }
}