package dev.panuszewski.template.extensions

fun subsequentNumbers(since: Int) =
    Tuple10(since, since + 1, since + 2, since + 3, since + 4, since + 5, since + 6, since + 7, since + 8, since + 9)

data class Tuple10(
    val item1: Int,
    val item2: Int,
    val item3: Int,
    val item4: Int,
    val item5: Int,
    val item6: Int,
    val item7: Int,
    val item8: Int,
    val item9: Int,
    val item10: Int,
)

