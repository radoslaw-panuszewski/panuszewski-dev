package dev.panuszewski.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.code2

// Data structures for representing files and their content
data class ProjectFile(
    val name: String,
    val path: String,
    val isFolder: Boolean = false,
    val content: String = "",
    val language: Language = Language.Kotlin,
    val children: List<ProjectFile> = emptyList()
)

@Composable
fun IDE(
    files: List<ProjectFile>,
    initialOpenFile: ProjectFile? = null,
    modifier: Modifier = Modifier
) {
    var currentOpenFile by remember { mutableStateOf(initialOpenFile ?: files.firstOrNull { !it.isFolder }) }
    var previousOpenFile by remember { mutableStateOf<ProjectFile?>(null) }

    Column(
        modifier = modifier
            .border(
                color = Color.Black,
                width = 1.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .fillMaxSize()
    ) {
        // IDE toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE2E2E2))
                .padding(vertical = 6.dp, horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .width(10.dp)
                    .height(10.dp)
                    .background(Color(0xFFFF605C), shape = RoundedCornerShape(50))
            )
            Box(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .width(10.dp)
                    .height(10.dp)
                    .background(Color(0xFFFFBD44), shape = RoundedCornerShape(50))
            )
            Box(
                modifier = Modifier
                    .width(10.dp)
                    .height(10.dp)
                    .background(Color(0xFF00CA4E), shape = RoundedCornerShape(50))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text("IDE", fontWeight = FontWeight.Bold)
        }

        Divider(Modifier.background(Color(0xFFA6A7A6)))

        // Main content
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFEFFFE))
        ) {
            // Project files panel (left)
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .background(Color(0xFFF3F3F3))
                    .border(width = 1.dp, color = Color(0xFFDDDDDD))
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(files) { file ->
                        FileItem(
                            file = file,
                            isSelected = file == currentOpenFile,
                            onClick = {
                                if (!file.isFolder && file != currentOpenFile) {
                                    previousOpenFile = currentOpenFile
                                    currentOpenFile = file
                                }
                            }
                        )
                    }
                }
            }

            // Code display panel (center)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (currentOpenFile != null && previousOpenFile != null) {
                    AnimatedContent(
                        targetState = currentOpenFile!!,
                        transitionSpec = {
                            slideInHorizontally { width -> width } + fadeIn() togetherWith
                                    slideOutHorizontally { width -> -width } + fadeOut()
                        }
                    ) { file ->
                        CodePanel(file = file)
                    }
                } else if (currentOpenFile != null) {
                    CodePanel(file = currentOpenFile!!)
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No file selected")
                    }
                }
            }
        }
    }
}

@Composable
private fun FileItem(
    file: ProjectFile,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) Color(0xFFD2E4FF) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Simple colored box instead of icon
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(
                    color = when {
                        file.isFolder -> Color(0xFFFFC107) // Folder color
                        file.language == Language.Kotlin -> Color(0xFF2196F3) // Kotlin file color
                        else -> Color(0xFF9E9E9E) // Other file color
                    },
                    shape = RoundedCornerShape(2.dp)
                )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = file.name,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun CodePanel(file: ProjectFile) {
    val annotatedString = buildAnnotatedString {
        append(file.content)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        code2 {
            Text(annotatedString)
        }
    }
}