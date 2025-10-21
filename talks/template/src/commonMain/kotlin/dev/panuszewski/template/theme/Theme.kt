package dev.panuszewski.template.theme

import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

data class CustomPalette(
    val orange: Color,
    val blue: Color,
    val green: Color,
    val pink: Color,
    val turquoise: Color,
    val darkTurquoise: Color,
    val purple: Color,
    val yellow: Color,
    val red: Color,
    val gray: Color,
    val lightGray: Color,
    val darkRed: Color,
    val treeNodeBackground: Color,
    val monospaceText: Color,
    val monospaceBackground: Color,
    val warningHighlight: Color
)

val palette: CustomPalette @Composable get() = LocalPalette.current

val DARK_PALETTE = CustomPalette(
    orange = Color(0xFFFF8A04),
    blue = Color(0xFF0A7CFA),
    green = Color(0xFF389E2D),
    pink = Color(0xFFDB26E0),
    turquoise = Color(0xFF39CEC2),
    darkTurquoise = Color(0xFF0E4A3E),
    purple = Color(0xFF9436FF),
    yellow = Color(0xFFFFE436),
    red = Color(0xFFFF6B68),
    gray = Color(0xFF555555),
    lightGray = Color.DarkGray,
    darkRed = Color(0xFF682F29),
    treeNodeBackground = Color(0xFF313438),
    monospaceText = Color.LightGray,
    monospaceBackground = Color.DarkGray,
    warningHighlight = Color(0xFFFDF4D4),
)

val LIGHT_PALETTE = CustomPalette(
    orange = Color(0xFFFF8A04),
    blue = Color(0xFF0A7CFA),
    green = Color(0xFF389E2D),
    pink = Color(0xFFDB26E0),
    turquoise = Color(54, 161, 165),
    darkTurquoise = Color(0xFF145F50),
    purple = Color(0xFFAC62F8),
    yellow = Color(0xFFB6A329),
    red = Color(0xFFF50100),
    gray = Color(0xFF555555),
    lightGray = Color.LightGray,
    darkRed = Color(0xFFC4574A),
    treeNodeBackground = Color(0xFFFCFCFC),
    monospaceText = Color.Black,
    monospaceBackground = Color(0xFFDADBDD),
    warningHighlight = Color(0xFFFDF4D4),
)

val LIGHT_COLORS = lightColors(
    primary = Color(54, 161, 165),
    primaryVariant = Color(0xFF145F50),
    secondary = Color(148, 45, 243),
    secondaryVariant = LIGHT_PALETTE.orange
)

val DARK_COLORS = darkColors(
    background = Color.Black,
    surface = Color(0xFF1E1F22),
    onBackground = Color(0xFFBCBEC4),
    primary = Color(0xFF0E4A3E),
    primaryVariant = Color(0xFF145F50),
    secondary = Color(0xFF942DF3),
    secondaryVariant = DARK_PALETTE.orange
)

val LIGHT_THEME = SceneDecorator { content ->
    MaterialTheme(LIGHT_COLORS, Typography()) {
        Surface {
            CompositionLocalProvider(
                LocalCodeStyle provides INTELLIJ_LIGHT_CODE_STYLE,
                LocalIdeColors provides LightIdeColors,
                LocalPalette provides LIGHT_PALETTE,
            ) {
                content()
            }
        }
    }
}

val DARK_THEME = SceneDecorator { content ->
    MaterialTheme(DARK_COLORS, Typography()) {
        Surface {
            CompositionLocalProvider(
                LocalCodeStyle provides INTELLIJ_DARK_CODE_STYLE,
                LocalIdeColors provides DarkIdeColors,
                LocalPalette provides DARK_PALETTE,
            ) {
                content()
            }
        }
    }
}

/**
 * Global theme state that can be toggled with Ctrl+T
 */
object ThemeState {
    var isDarkTheme by mutableStateOf(false)
    
    fun toggle() {
        isDarkTheme = !isDarkTheme
    }
}

/**
 * A theme decorator that can be toggled between light and dark themes using Ctrl+T
 */
val TOGGLEABLE_THEME = SceneDecorator { content ->
    val isDark = ThemeState.isDarkTheme
    
    val colors = if (isDark) DARK_COLORS else LIGHT_COLORS
    val codeStyle = if (isDark) INTELLIJ_DARK_CODE_STYLE else INTELLIJ_LIGHT_CODE_STYLE
    val ideColors = if (isDark) DarkIdeColors else LightIdeColors
    val palette = if (isDark) DARK_PALETTE else LIGHT_PALETTE
    
    MaterialTheme(colors, Typography()) {
        Surface {
            CompositionLocalProvider(
                LocalCodeStyle provides codeStyle,
                LocalIdeColors provides ideColors,
                LocalPalette provides palette,
            ) {
                content()
            }
        }
    }
}

val LocalCodeStyle = compositionLocalOf { INTELLIJ_LIGHT_CODE_STYLE }
val LocalPalette = compositionLocalOf { LIGHT_PALETTE }
val LocalIdeColors = compositionLocalOf { LightIdeColors }

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

data class IdeColorScheme(
    val toolbar: Color,
    val toolbarBorder: Color,
    val background: Color,
    val fileTreeBackground: Color,
    val fileTreeBorder: Color,
    val selectedFileBackground: Color,
    val highlightedFileBackground: Color,
    val tabBackground: Color,
    val selectedTabBackground: Color,
    val codePanelBackground: Color,
    val border: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val folderIcon: Color,
    val kotlinFileIcon: Color,
    val genericFileIcon: Color,
    val paneBackground: Color,
    val paneSeparator: Color
)

val LightIdeColors = IdeColorScheme(
    toolbar = Color(0xFFE2E2E2),
    toolbarBorder = Color(0xFFA6A7A6),
    background = Color(0xFFFEFFFE),
    fileTreeBackground = Color(0xFFF3F3F3),
    fileTreeBorder = Color(0xFFDDDDDD),
    selectedFileBackground = Color(0xFFD2E4FF),
    highlightedFileBackground = LIGHT_PALETTE.orange,
    tabBackground = Color(0xFFF2F2F2),
    selectedTabBackground = Color(0xFFD2E4FF),
    codePanelBackground = Color.White,
    border = Color.Black,
    textPrimary = Color(0xFF2C2C2C),
    textSecondary = Color(0xFF2C5BB7),
    folderIcon = Color(0xFFFFC107),
    kotlinFileIcon = Color(0xFF2196F3),
    genericFileIcon = Color(0xFF9E9E9E),
    paneBackground = Color(0xFFEEEEEE),
    paneSeparator = Color(0xFFCCCCCC)
)

val DarkIdeColors = IdeColorScheme(
    toolbar = Color(0xFF2B2D30),
    toolbarBorder = Color(0xFF5C5E61),
    background = Color(0xFF1E1F22),
    fileTreeBackground = Color(0xFF242629),
    fileTreeBorder = Color(0xFF5C5E61),
    selectedFileBackground = Color(0xFF2D4F7C),
    highlightedFileBackground = DARK_PALETTE.orange,
    tabBackground = Color(0xFF393B40),
    selectedTabBackground = Color(0xFF2D4F7C),
    codePanelBackground = Color(0xFF1E1F22),
    border = Color(0xFF5C5E61),
    textPrimary = Color(0xFFBCBEC4),
    textSecondary = Color(0xFF6FAFFF),
    folderIcon = Color(0xFFDCB67A),
    kotlinFileIcon = Color(0xFF7F52FF),
    genericFileIcon = Color(0xFF9AA7B0),
    paneBackground = Color(0xFF313438),
    paneSeparator = Color(0xFF5C5E61)
)

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
fun <T : Any> Builder.asCode(block: Builder.() -> T) {
    withStyle(
        style = SpanStyle(
            fontFamily = JetBrainsMono,
            color = palette.monospaceText,
            background = palette.monospaceBackground,
            fontSize = LocalTextStyle.current.fontSize * 0.9
        ),
        block = block
    )
}

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

@Composable
fun Builder.appendAsCode(text: String) {
    asCode { append(text) }
}