package dev.panuszewski.template.extensions

fun subsequentNumbers2(since: Int) =
    Tuple2(since, since + 1)

fun subsequentNumbers3(since: Int) =
    Tuple3(since, since + 1, since + 2)

fun subsequentNumbers4(since: Int) =
    Tuple4(since, since + 1, since + 2, since + 3)

fun subsequentNumbers5(since: Int) =
    Tuple5(since, since + 1, since + 2, since + 3, since + 4)

fun subsequentNumbers6(since: Int) =
    Tuple6(since, since + 1, since + 2, since + 3, since + 4, since + 5)

fun subsequentNumbers7(since: Int) =
    Tuple7(since, since + 1, since + 2, since + 3, since + 4, since + 5, since + 6)

fun subsequentNumbers8(since: Int) =
    Tuple8(since, since + 1, since + 2, since + 3, since + 4, since + 5, since + 6, since + 7)

fun subsequentNumbers9(since: Int) =
    Tuple9(since, since + 1, since + 2, since + 3, since + 4, since + 5, since + 6, since + 7, since + 8)

fun subsequentNumbers10(since: Int) =
    Tuple10(since, since + 1, since + 2, since + 3, since + 4, since + 5, since + 6, since + 7, since + 8, since + 9)

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

data class Tuple6(
    val item1: Int,
    val item2: Int,
    val item3: Int,
    val item4: Int,
    val item5: Int,
    val item6: Int,
)

data class Tuple7(
    val item1: Int,
    val item2: Int,
    val item3: Int,
    val item4: Int,
    val item5: Int,
    val item6: Int,
    val item7: Int,
)

data class Tuple8(
    val item1: Int,
    val item2: Int,
    val item3: Int,
    val item4: Int,
    val item5: Int,
    val item6: Int,
    val item7: Int,
    val item8: Int,
)

data class Tuple9(
    val item1: Int,
    val item2: Int,
    val item3: Int,
    val item4: Int,
    val item5: Int,
    val item6: Int,
    val item7: Int,
    val item8: Int,
    val item9: Int,
)

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

