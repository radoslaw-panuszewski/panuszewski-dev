package dev.panuszewski.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import dev.panuszewski.template.theme.palette
import dev.panuszewski.template.theme.withColor

enum class Design(val id: String, val label: String) {
    VIRTUAL_CONFIGS("3.1", "virtual configurations + DSL"),
    ANNOTATIONS("3.2", "real configurations + annotations"),
}

val Design.color: Color
    @Composable get() = when (this) {
        Design.VIRTUAL_CONFIGS -> palette.purple
        Design.ANNOTATIONS -> palette.green
    }

enum class Implementation(val id: String, val label: String) {
    TEAMCITY_PLUGIN("4.1", "TeamCity plugin"),
    DYNAMIC_BUILD_CHAINS("4.2", "Dynamic Build Chains"),
    SKIP_TAGS("4.3", "Skip tags + REST trigger"),
}

val Implementation.color: Color
    @Composable get() = when (this) {
        Implementation.TEAMCITY_PLUGIN -> palette.purple
        Implementation.DYNAMIC_BUILD_CHAINS -> palette.blue
        Implementation.SKIP_TAGS -> palette.green
    }

/** Appends e.g. "(3.1) virtual configurations + DSL ✅", always naming the design. */
@Composable
fun AnnotatedString.Builder.appendDesign(design: Design, verdict: String? = null) {
    withColor(design.color) {
        append("(${design.id}) ${design.label}")
    }
    if (verdict != null) append(" $verdict")
}

/** Appends e.g. "(4.3) Skip tags + REST trigger", always naming the implementation. */
@Composable
fun AnnotatedString.Builder.appendImplementation(
    implementation: Implementation,
    verdict: String? = null,
) {
    withColor(implementation.color) {
        append("(${implementation.id}) ${implementation.label}")
    }
    if (verdict != null) append(" $verdict")
}
