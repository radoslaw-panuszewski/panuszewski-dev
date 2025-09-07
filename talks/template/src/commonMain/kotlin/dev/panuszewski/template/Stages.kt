package dev.panuszewski.template

class Stages {
    var lastState = 0
        private set

    val stateCount get() = lastState + 1

    fun registerStatesByCount(start: Int, count: Int): List<Int> {
        return registerStatesByRange(start, start + count - 1)
    }

    fun registerStatesByRange(start: Int, end: Int): List<Int> {
        val stateList = start..end
        stateList.lastOrNull()?.let { lastState = it }
        return stateList.toList()
    }
}