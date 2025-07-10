package dev.bnorm.storyboard.easel.assist

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import dev.bnorm.storyboard.easel.StoryState

@Stable
class StoryAssistantState(
    internal val storyState: StoryState,
    captions: List<Caption> = emptyList(),
) {
    internal var visible by mutableStateOf(false)
    internal val captions = SnapshotStateList<Caption>().apply {
        addAll(captions)
    }
}
