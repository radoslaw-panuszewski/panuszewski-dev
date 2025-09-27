package dev.panuszewski.template.extensions

fun <K : Comparable<K>, V> sortedMapOf(vararg pairs: Pair<K, V>): Map<K, V> {
    val map = pairs.toMap()
    val keys = map.keys
    return keys.sorted().associateWith { map[it] ?: error("Key $it not found") }
}
