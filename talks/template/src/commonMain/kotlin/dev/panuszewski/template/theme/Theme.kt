package dev.panuszewski.template.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString.Builder
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import dev.bnorm.storyboard.SceneDecorator
import dev.panuszewski.template.extensions.JetBrainsMono

val NICE_ORANGE = Color(0xFFFF8A04)
val NICE_BLUE = Color(0xFF0A7CFA)
val NICE_GREEN = Color(0xFF389E2D)

val DARK_THEME = SceneDecorator { content ->
    MaterialTheme(DARK_COLORS, Typography()) {
        Surface {
            CompositionLocalProvider(LocalCodeStyle provides INTELLIJ_DARK_CODE_STYLE) {
                content()
            }
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
    secondaryVariant = NICE_ORANGE
)

val LIGHT_THEME = SceneDecorator { content ->
    MaterialTheme(LIGHT_COLORS, Typography()) {
        Surface {
            CompositionLocalProvider(LocalCodeStyle provides INTELLIJ_LIGHT_CODE_STYLE) {
                content()
            }
        }
    }
}

val LocalCodeStyle = compositionLocalOf { INTELLIJ_LIGHT_CODE_STYLE }

val LIGHT_COLORS = lightColors(
    primary = Color(54, 161, 165),
    primaryVariant = Color(0xFF145F50),
    secondary = Color(148, 45, 243),
    secondaryVariant = NICE_ORANGE
)

val primaryColor: Color
    @Composable
    get() = MaterialTheme.colors.primary

val secondaryColor: Color
    @Composable
    get() = MaterialTheme.colors.secondary

val primaryVariantColor: Color
    @Composable
    get() = MaterialTheme.colors.primaryVariant

val secondaryVariantColor: Color
    @Composable
    get() = MaterialTheme.colors.secondaryVariant

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

@Composable
fun <T : Any> Builder.withColor(color: Color, block: Builder.() -> T) {
    withStyle(SpanStyle(color = color), block)
}

@Composable
fun <T : Any> Builder.withPrimaryColor(block: Builder.() -> T) {
    withStyle(SpanStyle(color = MaterialTheme.colors.primary), block)
}

@Composable
fun <T : Any> Builder.withPrimaryVariantColor(block: Builder.() -> T) {
    withStyle(SpanStyle(color = MaterialTheme.colors.primaryVariant), block)
}

@Composable
fun <T : Any> Builder.withSecondaryColor(block: Builder.() -> T) {
    withStyle(SpanStyle(color = MaterialTheme.colors.secondary), block)
}

@Composable
fun <T : Any> Builder.withSecondaryVariantColor(block: Builder.() -> T) {
    withStyle(SpanStyle(color = MaterialTheme.colors.secondaryVariant), block)
}

@Composable
fun Builder.appendWithColor(color: Color, text: String) {
    withColor(color) { append(text) }
}

@Composable
fun Builder.appendWithPrimaryColor(text: String) {
    withPrimaryColor { append(text) }
}

@Composable
fun Builder.appendWithPrimaryVariantColor(text: String) {
    withPrimaryVariantColor { append(text) }
}

@Composable
fun Builder.appendWithSecondaryColor(text: String) {
    withSecondaryColor { append(text) }
}

@Composable
fun Builder.appendWithSecondaryVariantColor(text: String) {
    withSecondaryVariantColor { append(text) }
}