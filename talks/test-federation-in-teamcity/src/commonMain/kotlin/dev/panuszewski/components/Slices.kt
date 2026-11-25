package dev.panuszewski.components

/**
 * Canonical test-slice labels.
 *
 * [dev.panuszewski.template.components.AnimatedHorizontalTree] keys its shared elements by the node
 * value, so a label must be byte-identical across states to animate instead of cross-fading.
 * Always build node labels through these helpers — hand-typed emoji easily pick up a stray or
 * doubled variation selector (U+FE0F), which silently breaks the transition.
 */
const val ALL_TESTS_EMOJI = "\uD83D\uDEE1\uFE0F"
const val SMOKE_TESTS_EMOJI = "\uD83D\uDCA8"
const val CONTRACT_TESTS_EMOJI = "\uD83E\uDD1D"

fun allTests(name: String) = "$ALL_TESTS_EMOJI $name"
fun smokeTests(name: String) = "$SMOKE_TESTS_EMOJI $name"
fun contractTests(name: String) = "$CONTRACT_TESTS_EMOJI $name"
