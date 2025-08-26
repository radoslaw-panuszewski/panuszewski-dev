package dev.panuszewski.template

fun <T> List<T>.safeGet(index: Int): T = this[index.coerceIn(indices)]
