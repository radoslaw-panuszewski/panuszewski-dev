package dev.panuszewski.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.panuszewski.template.code3
import kotlinx.coroutines.delay
import kotlin.math.max

@Composable
fun Terminal(textsToDisplay: List<String>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .border(
                color = Color.Black,
                width = 1.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .width(400.dp)
            .height(275.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF1F0EE))
                .padding(vertical = 6.dp, horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(10.dp)
                    .background(Color(0xFFFF605C), shape = RoundedCornerShape(50))
            )
            Box(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(10.dp)
                    .background(Color(0xFFFFBD44), shape = RoundedCornerShape(50))
            )
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color(0xFF00CA4E), shape = RoundedCornerShape(50))
            )
        }

        Divider(Modifier.background(Color(0xFFA6A7A6)))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFEFFFE))
                .padding(horizontal = 16.dp)
        ) {

            val columnState = rememberLazyListState()

            LaunchedEffect(textsToDisplay.size) {
                columnState.animateScrollToItem(max(0, textsToDisplay.lastIndex))
            }

            val texts = textsToDisplay.filter { it.isNotBlank() }

            LazyColumn(state = columnState, modifier = Modifier.padding(bottom = 16.dp)) {
                for ((index, text) in texts.withIndex()) {

                    item {
                        if (text.startsWith("$")) {
                            var displayedText by remember { mutableStateOf("") }
                            LaunchedEffect(Unit) {
                                for (i in 0..text.length) {
                                    displayedText = text.take(i)
                                    delay(10)
                                }
                            }
                            Spacer(Modifier.height(16.dp))
                            code3 { Text(displayedText) }
                        } else {
                            Spacer(Modifier.height(16.dp))
                            code3 { Text(text, color = Color(53, 140, 142)) }
                        }

                        if (index == texts.lastIndex) {
                            Spacer(Modifier.height(50.dp))
                        }
                    }
                }
            }
        }
    }
}