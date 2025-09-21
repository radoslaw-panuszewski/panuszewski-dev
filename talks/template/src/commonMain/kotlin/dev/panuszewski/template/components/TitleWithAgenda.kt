package dev.panuszewski.template.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder

fun StoryboardBuilder.TitleWithAgenda(title: String, agenda: List<String>) =
    TitleWithAgenda(
        title = AnnotatedString(title),
        agenda = agenda.map(::AnnotatedString)
    )

fun StoryboardBuilder.TitleWithAgenda(title: AnnotatedString, agenda: List<AnnotatedString>) {
    scene(stateCount = 5) {
        SlidingTitleScaffold(title, slideBackAt = 4) {
            Box {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    for (agendaItem in agenda) {
                        Text(agendaItem)
                    }
                }
            }
        }
    }
}
