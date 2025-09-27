package dev.panuszewski.template.extensions

fun subsequentNumbers2(since: Int) =
    Tuple2(since, since + 1)

fun subsequentNumbers3(since: Int) =
    Tuple3(since, since + 1, since + 2)

fun subsequentNumbers4(since: Int) =
    Tuple4(since, since + 1, since + 2, since + 3)

fun subsequentNumbers5(since: Int) =
    Tuple5(since, since + 1, since + 2, since + 3, since + 4)

data class Tuple2(
    val item1: Int,
    val item2: Int,
)

data class Tuple3(
    val item1: Int,
    val item2: Int,
    val item3: Int,
)

data class Tuple4(
    val item1: Int,
    val item2: Int,
    val item3: Int,
    val item4: Int,
)

data class Tuple5(
    val item1: Int,
    val item2: Int,
    val item3: Int,
    val item4: Int,
    val item5: Int,
)
