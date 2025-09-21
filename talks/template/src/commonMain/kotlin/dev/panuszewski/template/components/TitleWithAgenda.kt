package dev.panuszewski.template.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility

fun StoryboardBuilder.TitleWithAgenda(title: String, agenda: List<String>) =
    TitleWithAgenda(
        title = AnnotatedString(title),
        agenda = agenda.map(::AnnotatedString)
    )

fun StoryboardBuilder.TitleWithAgenda(title: AnnotatedString, agenda: List<AnnotatedString>) {
    scene(stateCount = 5) {
        SlidingTitleScaffold(title) {
            Box(modifier = Modifier.offset(y = -50.dp)) {
                transition.SlideFromBottomAnimatedVisibility({ it.toState() == 2 }) {
                    Column(
                        Modifier.padding(vertical = 32.dp, horizontal = 32.dp),
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
}
