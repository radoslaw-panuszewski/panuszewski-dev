package dev.panuszewski.template.extensions

fun <T> List<T>.safeGet(index: Int): T =
    this[index.coerceIn(indices)]
