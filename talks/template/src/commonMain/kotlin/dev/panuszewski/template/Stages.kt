package dev.panuszewski.template

class Stages {
    var lastState = 0
        private set

    val stateCount get() = lastState + 1

    fun registerStates(since: Int, count: Int): List<Int> {
        val stateList = (since until since + count).toList()
        stateList.lastOrNull()?.let { lastState = it }
        return stateList
    }
}