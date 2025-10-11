package dev.panuszewski.template.extensions

fun subsequentNumbers(since: Int = 0) =
    Tuple(
        since,
        since + 1,
        since + 2,
        since + 3,
        since + 4,
        since + 5,
        since + 6,
        since + 7,
        since + 8,
        since + 9,
        since + 10,
        since + 11,
        since + 12,
        since + 13,
        since + 14,
        since + 15,
        since + 16,
        since + 17,
        since + 18,
        since + 19,
    )

data class Tuple(
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
    val item11: Int,
    val item12: Int,
    val item13: Int,
    val item14: Int,
    val item15: Int,
    val item16: Int,
    val item17: Int,
    val item18: Int,
    val item19: Int,
    val item20: Int,
)

