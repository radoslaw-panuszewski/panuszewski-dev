package dev.panuszewski.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import dev.panuszewski.template.theme.palette
import dev.panuszewski.template.theme.withColor

/**
 * @param label the requirement as written in the design doc
 * @param short a tighter wording for dense option cards — still names the requirement, so a
 *   reference never degrades to a bare number the audience has to remember
 */
enum class Requirement(val id: String, val label: String, val short: String) {
    PREDICTABLE_STATS("1.1", "Predictable TeamCity build statistics", "Predictable statistics"),
    BREADCRUMBS("1.2", "Bidirectional Breadcrumbs", "Bidirectional breadcrumbs"),
    RESOURCE_USAGE("1.3", "Reasonable resource usage", "Reasonable resource usage"),
    MENTAL_MODEL("1.4", "Intuitive mental model", "Intuitive mental model"),
    TEAMCITY_UX("1.5", "Clear TeamCity UX", "Clear TeamCity UX"),
    PERFORMANCE("1.6", "Existing build performance", "Existing build performance"),
}

val Requirement.color: Color
    @Composable get() = when (this) {
        Requirement.PREDICTABLE_STATS -> palette.orange
        Requirement.BREADCRUMBS -> palette.blue
        Requirement.RESOURCE_USAGE -> palette.green
        Requirement.MENTAL_MODEL -> palette.purple
        Requirement.TEAMCITY_UX -> palette.turquoise
        Requirement.PERFORMANCE -> palette.yellow
    }

/** Appends the doc wording, e.g. "(1.1) Predictable TeamCity build statistics ✅". */
@Composable
fun AnnotatedString.Builder.appendRequirement(requirement: Requirement, verdict: String? = null) {
    withColor(requirement.color) {
        append("(${requirement.id}) ${requirement.label}")
    }
    if (verdict != null) append(" $verdict")
}

/** Appends the tighter wording for option cards, e.g. "(1.1) Predictable statistics ✅". */
@Composable
fun AnnotatedString.Builder.appendRequirementShort(
    requirement: Requirement,
    verdict: String? = null,
) {
    withColor(requirement.color) {
        append("(${requirement.id}) ${requirement.short}")
    }
    if (verdict != null) append(" $verdict")
}
