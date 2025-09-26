package com.example.games.sudoku.common

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CountUpTimer {
    private var job: Job? = null
    private val _second = MutableStateFlow(0)
    val second: StateFlow<Int> = _second

    fun start() {
        // cancela si ya había uno
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                delay(1000L)
                _second.value += 1
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
    }

    fun reset() {
        stop()
        _second.value = 0
    }
}
