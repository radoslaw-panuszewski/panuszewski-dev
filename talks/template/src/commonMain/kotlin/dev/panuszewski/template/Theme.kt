package dev.panuszewski.template

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.bnorm.storyboard.SceneDecorator

val DARK_THEME = SceneDecorator { content ->
    MaterialTheme(DARK_COLORS, Typography()) {
        Surface {
            content()
        }
    }
}

val DARK_COLORS = darkColors(
    background = Color.Black,
    surface = Color(0xFF1E1F22),
    onBackground = Color(0xFFBCBEC4),
    primary = Color(0xFF0E4A3E),
    primaryVariant = Color(0xFF145F50),
    secondary = Color(0xFF942DF3),
)

val LIGHT_THEME = SceneDecorator { content ->
    MaterialTheme(LIGHT_COLORS, Typography()) {
        Surface {
            content()
        }
    }
}

val LIGHT_COLORS = lightColors(
    primary = Color(0xFF0E4A3E),
    primaryVariant = Color(0xFF145F50),
    secondary = Color(0xFF942DF3),
)

val Typography.code1: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    )

val Typography.code2: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.25.sp
    )

val Typography.code3: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    )

const val BULLET_1 = " • "
const val BULLET_2 = " ◦ "
const val BULLET_3 = " ‣ "