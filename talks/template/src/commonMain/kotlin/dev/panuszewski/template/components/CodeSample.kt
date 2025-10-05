package dev.panuszewski.template.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import dev.bnorm.storyboard.text.TextTag
import dev.bnorm.storyboard.text.TextTagScope
import dev.bnorm.storyboard.text.addStyleByTag
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.magic.splitByWords
import dev.bnorm.storyboard.text.replaceAllByTag
import dev.panuszewski.template.extensions.TagType
import dev.panuszewski.template.theme.LocalCodeStyle
import dev.panuszewski.template.extensions.toCode
import kotlin.collections.iterator

@Immutable
class CodeSample private constructor(
    private val focus: List<TextTag>,
    private val focusedStyle: SpanStyle? = FOCUSED_STYLE,
    private val unfocusedStyle: SpanStyle? = UNFOCUSED_STYLE,
    private val replaced: Map<TextTag, AnnotatedString>,
    private val styled: Map<TextTag, SpanStyle>,
    private val scrollTag: TextTag?,
    val data: Any?,
    val text: AnnotatedString,
    val language: Language,
    val title: String?,
    val splitMethod: (AnnotatedString) -> List<AnnotatedString>,
    val warningTags: List<TextTag> = emptyList()
) {
    constructor(text: AnnotatedString, language: Language, title: String? = null, splitMethod: (AnnotatedString) -> List<AnnotatedString> = { it.splitByWords() })
            : this(emptyList(), FOCUSED_STYLE, UNFOCUSED_STYLE, emptyMap(), emptyMap(), null, null, text, language, title, splitMethod)

    @Composable
    fun Split() = splitMethod(String())

    @Composable
    fun String(): AnnotatedString {
        var str = text.toCode(language, LocalCodeStyle.current, CodeScope.File, { _, _ -> null })
        for ((tag, style) in styled) {
            str = str.addStyleByTag(listOf(tag), tagged = style)
        }
        if (focus.isNotEmpty()) {
            str = str.addStyleByTag(focus, tagged = focusedStyle, untagged = unfocusedStyle)
        }
        // TODO collapse first and then hide?
        //  - will keep the collapses from being split,
        //    which results in adjacent collapses
        //  - or maybe the replaceAll can manage this by merging them together?
        for ((tag, replacement) in replaced) {
            str = str.replaceAllByTag(tag, replacement)
        }
        return str
    }

    @Composable
    fun Scroll(): Int {
        if (scrollTag == null) return 0

        val string = String()

        val offset = string.getStringAnnotations(scrollTag.annotationStringTag, 0, string.length)
            .filter { it.item == scrollTag.id }
            .minOfOrNull { it.start } ?: 0

        return string.text.substring(0, offset).count { it == '\n' }
    }

    companion object {
        private val FOCUSED_STYLE = SpanStyle(color = Color(0xFFFF8A04))
        private val UNFOCUSED_STYLE = SpanStyle(color = Color(0xFF555555))
        private val ELLIPSIS = AnnotatedString(" … ", spanStyle = UNFOCUSED_STYLE)
        private val EMPTY = AnnotatedString("")
        private val ERROR_STYLE_LIGHT_THEME = SpanStyle(color = Color(0xFFF50100))
        private val ERROR_STYLE_DARK_THEME = SpanStyle(color = Color(0xFFFF6B68))

        internal fun create(
            text: AnnotatedString,
            language: Language,
            title: String? = null,
            splitMethod: (AnnotatedString) -> List<AnnotatedString> = { it.splitByWords() },
            warningTags: List<TextTag> = emptyList()
        ): CodeSample = CodeSample(
            focus = emptyList(),
            focusedStyle = FOCUSED_STYLE,
            unfocusedStyle = UNFOCUSED_STYLE,
            replaced = emptyMap(),
            styled = emptyMap(),
            scrollTag = null,
            data = null,
            text = text,
            language = language,
            title = title,
            splitMethod = splitMethod,
            warningTags = warningTags
        )
    }

    private fun copy(
        focus: List<TextTag> = this.focus,
        focusedStyle: SpanStyle? = this.focusedStyle,
        unfocusedStyle: SpanStyle? = this.unfocusedStyle,
        replaced: Map<TextTag, AnnotatedString> = this.replaced,
        styled: Map<TextTag, SpanStyle> = this.styled,
        scrollTag: TextTag? = this.scrollTag,
        data: Any? = this.data,
        text: AnnotatedString = this.text,
        language: Language = this.language,
        title: String? = this.title,
        splitMethod: (AnnotatedString) -> List<AnnotatedString> = this.splitMethod,
        warningTags: List<TextTag> = this.warningTags,
    ): CodeSample = CodeSample(focus, focusedStyle, unfocusedStyle, replaced, styled, scrollTag, data, text, language, title, splitMethod, warningTags)

    fun collapse(tag: TextTag): CodeSample = copy(replaced = replaced + (tag to ELLIPSIS))
    fun collapse(vararg tags: TextTag): CodeSample = collapse(tags.asList())
    fun collapse(tags: List<TextTag>): CodeSample {
        if (tags.isEmpty()) return this
        return copy(replaced = replaced + tags.map { it to ELLIPSIS })
    }

    fun hide(tag: TextTag): CodeSample = copy(replaced = replaced + (tag to EMPTY))
    fun hide(vararg tags: TextTag): CodeSample = hide(tags.asList())
    fun hide(tags: List<TextTag>): CodeSample {
        if (tags.isEmpty()) return this
        return copy(replaced = replaced + tags.map { it to EMPTY })
    }

    fun reveal(tag: TextTag): CodeSample = copy(replaced = replaced - tag)
    fun reveal(vararg tags: TextTag): CodeSample = reveal(tags.asList())
    fun reveal(tags: List<TextTag>): CodeSample {
        if (tags.isEmpty()) return this
        return copy(replaced = replaced - tags)
    }

    fun revealAndFocus(vararg tags: TextTag) =
        reveal(*tags).focus(*tags)

    fun focus(tags: List<TextTag>, scroll: Boolean = true, focusedStyle: SpanStyle? = FOCUSED_STYLE, unfocusedStyle: SpanStyle? = UNFOCUSED_STYLE): CodeSample =
        copy(focus = tags, scrollTag = if (scroll) tags.first() else scrollTag, focusedStyle = focusedStyle, unfocusedStyle = unfocusedStyle)

    fun underline(tag: TextTag): CodeSample =
        changeTagType(tag, TagType.WARNING)

    fun resetUnderline(tag: TextTag): CodeSample =
        changeTagType(tag, TagType.NORMAL)

    fun changeTagType(tag: TextTag, newType: TagType): CodeSample {
        val updatedWarningTags = when (newType) {
            TagType.WARNING -> {
                // Add tag to warning tags if not already present
                if (tag in warningTags) {
                    warningTags
                } else {
                    warningTags + tag
                }
            }
            else -> {
                // Remove tag from warning tags if present
                warningTags - tag
            }
        }

        return copy(warningTags = updatedWarningTags)
    }

    fun changeTagType(tags: List<TextTag>, newType: TagType): CodeSample {
        if (tags.isEmpty()) return this
        var result = this
        for (tag in tags) {
            result = result.changeTagType(tag, newType)
        }
        return result
    }

    fun focus(vararg tags: TextTag, scroll: Boolean = true, focusedStyle: SpanStyle? = FOCUSED_STYLE, unfocusedStyle: SpanStyle? = UNFOCUSED_STYLE): CodeSample =
        focus(tags.asList(), scroll, focusedStyle, unfocusedStyle)

    fun focus(tag: TextTag, scroll: Boolean = true, focusedStyle: SpanStyle? = FOCUSED_STYLE, unfocusedStyle: SpanStyle? = UNFOCUSED_STYLE): CodeSample =
        focus(listOf(tag), scroll, focusedStyle, unfocusedStyle)

    fun focusNoStyling(vararg tags: TextTag, scroll: Boolean = true): CodeSample =
        focus(tags.asList(), scroll, null, null)

    fun focusNoScroll(vararg tags: TextTag, focusedStyle: SpanStyle? = FOCUSED_STYLE, unfocusedStyle: SpanStyle? = UNFOCUSED_STYLE): CodeSample =
        focus(tags.asList(), false, focusedStyle, unfocusedStyle)

    fun highlightAsError(vararg tags: TextTag, scroll: Boolean = false) =
        focus(tags.asList(), scroll, ERROR_STYLE_DARK_THEME, null)

    fun unfocus(unscroll: Boolean = true): CodeSample =
        copy(focus = emptyList(), scrollTag = if (unscroll) null else scrollTag)

    fun styled(tag: TextTag, style: SpanStyle): CodeSample = copy(styled = styled + (tag to style))
    fun styled(vararg tags: TextTag, style: SpanStyle): CodeSample = styled(tags.asList(), style)
    fun styled(tags: List<TextTag>, style: SpanStyle): CodeSample =
        copy(styled = styled + tags.associateWith { style })

    fun unstyled(tag: TextTag): CodeSample = copy(styled = styled - tag)

    fun scroll(tag: TextTag?): CodeSample = copy(scrollTag = tag)

    fun attach(data: Any?): CodeSample {
        if (this.data == data) return this
        return copy(data = data)
    }

    fun changeLanguage(language: Language): CodeSample = copy(language = language)

    fun changeTitle(title: String?): CodeSample = copy(title = title)
    
    fun openErrorWindow(text: String): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(OpenErrorWindow(text)))
    }
    
    fun closeErrorWindow(): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(CloseErrorWindow))
    }
    
    fun closeRightPane(): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(CloseRightPane))
    }
    
    fun closeLeftPane(): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(CloseLeftPane))
    }
    
    fun showFileTree(): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(ShowFileTree))
    }
    
    fun hideFileTree(): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(HideFileTree))
    }
    
    fun showEmoji(emoji: String): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(ShowEmoji(emoji)))
    }
    
    fun hideEmoji(): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(HideEmoji))
    }
    
    fun openInLeftPane(fileName: String, switchTo: Boolean = false): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(OpenInLeftPane(fileName, switchTo)))
    }
    
    fun openInRightPane(fileName: String, switchTo: Boolean = false): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(OpenInRightPane(fileName, switchTo)))
    }
    
    fun openTopPanel(name: String = "default"): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(OpenTopPanel(name)))
    }
    
    fun closeTopPanel(name: String = "default"): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(CloseTopPanel(name)))
    }
    
    fun openLeftPanel(name: String = "default"): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(OpenLeftPanel(name)))
    }
    
    fun closeLeftPanel(name: String = "default"): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(CloseLeftPanel(name)))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CodeSample

        if (focus != other.focus) return false
        if (replaced != other.replaced) return false
        if (styled != other.styled) return false
        if (scrollTag != other.scrollTag) return false
        if (data != other.data) return false
        if (warningTags != other.warningTags) return false
        return true
    }

    override fun hashCode(): Int {
        var result = text.hashCode()
        result = 31 * result + (focus?.hashCode() ?: 0)
        result = 31 * result + replaced.hashCode()
        result = 31 * result + styled.hashCode()
        result = 31 * result + (scrollTag?.hashCode() ?: 0)
        result = 31 * result + (data?.hashCode() ?: 0)
        result = 31 * result + warningTags.hashCode()
        return result
    }
}

fun <R> buildCodeSamples(builder: CodeSamplesBuilder.() -> R): R =
    CodeSamplesBuilder().builder()

data class SwitchToFile(val fileName: String)

data class ShowEmoji(val emoji: String)

object HideEmoji

data class OpenTopPanel(val name: String)

data class CloseTopPanel(val name: String)

data class OpenLeftPanel(val name: String)

data class CloseLeftPanel(val name: String)

data class OpenInLeftPane(val fileName: String, val switchTo: Boolean)

data class OpenInRightPane(val fileName: String, val switchTo: Boolean)

object CloseLeftPane

object CloseRightPane

object HideFileTree

object ShowFileTree

data class OpenErrorWindow(val text: String)

object CloseErrorWindow

data class AdvanceTogetherWith(val fileName: String)

data class ChainedOperations(val operations: List<Any>)

class CodeSampleWithIdeOps(
    val codeSample: CodeSample,
    val ideOperations: MutableList<Any>
) {
    fun openErrorWindow(text: String): CodeSampleWithIdeOps {
        ideOperations.add(OpenErrorWindow(text))
        return this
    }
    
    fun closeErrorWindow(): CodeSampleWithIdeOps {
        ideOperations.add(CloseErrorWindow)
        return this
    }
    
    fun closeRightPane(): CodeSampleWithIdeOps {
        ideOperations.add(CloseRightPane)
        return this
    }
    
    fun closeLeftPane(): CodeSampleWithIdeOps {
        ideOperations.add(CloseLeftPane)
        return this
    }
    
    fun showFileTree(): CodeSampleWithIdeOps {
        ideOperations.add(ShowFileTree)
        return this
    }
    
    fun hideFileTree(): CodeSampleWithIdeOps {
        ideOperations.add(HideFileTree)
        return this
    }
    
    fun showEmoji(emoji: String): CodeSampleWithIdeOps {
        ideOperations.add(ShowEmoji(emoji))
        return this
    }
    
    fun hideEmoji(): CodeSampleWithIdeOps {
        ideOperations.add(HideEmoji)
        return this
    }
    
    fun openInLeftPane(fileName: String, switchTo: Boolean = false): CodeSampleWithIdeOps {
        ideOperations.add(OpenInLeftPane(fileName, switchTo))
        return this
    }
    
    fun openInRightPane(fileName: String, switchTo: Boolean = false): CodeSampleWithIdeOps {
        ideOperations.add(OpenInRightPane(fileName, switchTo))
        return this
    }
    
    fun openTopPanel(name: String = "default"): CodeSampleWithIdeOps {
        ideOperations.add(OpenTopPanel(name))
        return this
    }
    
    fun closeTopPanel(name: String = "default"): CodeSampleWithIdeOps {
        ideOperations.add(CloseTopPanel(name))
        return this
    }
    
    fun openLeftPanel(name: String = "default"): CodeSampleWithIdeOps {
        ideOperations.add(OpenLeftPanel(name))
        return this
    }
    
    fun closeLeftPanel(name: String = "default"): CodeSampleWithIdeOps {
        ideOperations.add(CloseLeftPanel(name))
        return this
    }
}

data class InitiallyHiddenFile(val codeSamples: List<CodeSample>)

data class Directory(val isInitiallyHidden: Boolean = false)

val DIRECTORY = Directory()

fun List<CodeSample>.initiallyHidden() = InitiallyHiddenFile(this)
fun Directory.initiallyHidden() = copy(isInitiallyHidden = true)

class CodeSamplesBuilder : TextTagScope.Default() {
    fun String.toCodeSample(
        language: Language = Language.Kotlin,
        title: String? = null,
        splitMethod: (AnnotatedString) -> List<AnnotatedString> = { it.splitByWords() },
    ): CodeSample {
        val warningTags = tags.filter { it.data == TagType.WARNING }
        return CodeSample.create(extractTags(this), language, title, splitMethod, warningTags)
    }

    fun AnnotatedString.toCodeSample(
        language: Language = Language.Kotlin,
        title: String? = null,
        splitMethod: (AnnotatedString) -> List<AnnotatedString> = { it.splitByWords() },
    ): CodeSample {
        val warningTags = tags.filter { it.data == TagType.WARNING }
        return CodeSample.create(this, language, title, splitMethod, warningTags)
    }

    fun CodeSample.collapse(data: Any?): CodeSample = collapse(tags.filter { data == it.data })
    fun CodeSample.hide(data: Any?): CodeSample = hide(tags.filter { data == it.data })
    fun CodeSample.reveal(data: Any?): CodeSample = reveal(tags.filter { data == it.data })

    fun CodeSample.changeTagType(tag: TextTag, newType: TagType): CodeSample = this.changeTagType(tag, newType)
    fun CodeSample.changeTagType(data: Any?, newType: TagType): CodeSample = this.changeTagType(tags.filter { data == it.data }, newType)

    fun CodeSample.then(transformer: CodeSample.() -> Any): List<CodeSample> {
        val result = transformer(this)
        return when (result) {
            is CodeSampleWithIdeOps -> {
                val sampleWithChainedOps = result.codeSample.attach(ChainedOperations(result.ideOperations))
                listOf(this, sampleWithChainedOps)
            }
            is CodeSample -> listOf(this, result)
            else -> throw IllegalArgumentException("Transformer must return CodeSample or CodeSampleWithIdeOps")
        }
    }

    fun List<CodeSample>.then(transformer: CodeSample.() -> Any): List<CodeSample> {
        val lastSample = this.last()
        val cleanedSample = when (lastSample.data) {
            is SwitchToFile, is ShowEmoji, is HideEmoji,
            is OpenInLeftPane, is OpenInRightPane,
            is CloseLeftPane, is CloseRightPane,
            is HideFileTree, is ShowFileTree,
            is OpenErrorWindow, is CloseErrorWindow,
            is AdvanceTogetherWith, is ChainedOperations -> lastSample.attach(null)
            else -> lastSample
        }
        val result = transformer(cleanedSample)
        return when (result) {
            is CodeSampleWithIdeOps -> {
                val sampleWithChainedOps = result.codeSample.attach(ChainedOperations(result.ideOperations))
                this + sampleWithChainedOps
            }
            is CodeSample -> this + result
            else -> throw IllegalArgumentException("Transformer must return CodeSample or CodeSampleWithIdeOps")
        }
    }

    fun List<CodeSample>.thenTogetherWith(fileName: String, transformer: CodeSample.() -> CodeSample): List<CodeSample> {
        val lastSample = this.last()
        val cleanedSample = when (lastSample.data) {
            is SwitchToFile, is ShowEmoji, is HideEmoji,
            is OpenInLeftPane, is OpenInRightPane,
            is CloseLeftPane, is CloseRightPane,
            is HideFileTree, is ShowFileTree,
            is OpenErrorWindow, is CloseErrorWindow,
            is AdvanceTogetherWith, is ChainedOperations -> lastSample.attach(null)
            else -> lastSample
        }
        val markerSample = cleanedSample.attach(AdvanceTogetherWith(fileName))
        return this + transformer(markerSample)
    }

    fun List<CodeSample>.switchTo(fileName: String): List<CodeSample> {
        return this + last().attach(SwitchToFile(fileName))
    }

    fun List<CodeSample>.showEmoji(emoji: String): List<CodeSample> {
        return this + last().attach(ShowEmoji(emoji))
    }

    fun List<CodeSample>.hideEmoji(): List<CodeSample> {
        return this + last().attach(HideEmoji)
    }

    fun List<CodeSample>.openTopPanel(name: String = "default"): List<CodeSample> {
        return this + last().attach(OpenTopPanel(name))
    }

    fun List<CodeSample>.closeTopPanel(name: String = "default"): List<CodeSample> {
        return this + last().attach(CloseTopPanel(name))
    }

    fun List<CodeSample>.openLeftPanel(name: String = "default"): List<CodeSample> {
        return this + last().attach(OpenLeftPanel(name))
    }

    fun List<CodeSample>.closeLeftPanel(name: String = "default"): List<CodeSample> {
        return this + last().attach(CloseLeftPanel(name))
    }

    fun List<CodeSample>.openInLeftPane(fileName: String, switchTo: Boolean = false): List<CodeSample> {
        return this + last().attach(OpenInLeftPane(fileName, switchTo))
    }

    fun List<CodeSample>.openInRightPane(fileName: String, switchTo: Boolean = false): List<CodeSample> {
        return this + last().attach(OpenInRightPane(fileName, switchTo))
    }

    fun List<CodeSample>.closeLeftPane(): List<CodeSample> {
        return this + last().attach(CloseLeftPane)
    }

    fun List<CodeSample>.closeRightPane(): List<CodeSample> {
        return this + last().attach(CloseRightPane)
    }

    fun List<CodeSample>.hideFileTree(): List<CodeSample> {
        return this + last().attach(HideFileTree)
    }

    fun List<CodeSample>.showFileTree(): List<CodeSample> {
        return this + last().attach(ShowFileTree)
    }

    fun List<CodeSample>.openErrorWindow(text: String): List<CodeSample> {
        return this + last().attach(OpenErrorWindow(text))
    }

    fun List<CodeSample>.closeErrorWindow(): List<CodeSample> {
        return this + last().attach(CloseErrorWindow)
    }

    fun List<CodeSample>.instead(transformer: CodeSample.() -> CodeSample): List<CodeSample> {
        return this.subList(fromIndex = 0, toIndex = lastIndex) + transformer(this.last())
    }
}

class ChainableOperations(val operations: MutableList<Any> = mutableListOf()) {
    fun closeRightPane(): ChainableOperations {
        operations.add(CloseRightPane)
        return this
    }
    
    fun closeLeftPane(): ChainableOperations {
        operations.add(CloseLeftPane)
        return this
    }
    
    fun showFileTree(): ChainableOperations {
        operations.add(ShowFileTree)
        return this
    }
    
    fun hideFileTree(): ChainableOperations {
        operations.add(HideFileTree)
        return this
    }
    
    fun showEmoji(emoji: String): ChainableOperations {
        operations.add(ShowEmoji(emoji))
        return this
    }
    
    fun hideEmoji(): ChainableOperations {
        operations.add(HideEmoji)
        return this
    }
    
    fun openInLeftPane(fileName: String, switchTo: Boolean = false): ChainableOperations {
        operations.add(OpenInLeftPane(fileName, switchTo))
        return this
    }
    
    fun openInRightPane(fileName: String, switchTo: Boolean = false): ChainableOperations {
        operations.add(OpenInRightPane(fileName, switchTo))
        return this
    }
    
    fun openErrorWindow(text: String): ChainableOperations {
        operations.add(OpenErrorWindow(text))
        return this
    }
    
    fun closeErrorWindow(): ChainableOperations {
        operations.add(CloseErrorWindow)
        return this
    }
    
    fun openTopPanel(name: String = "default"): ChainableOperations {
        operations.add(OpenTopPanel(name))
        return this
    }
    
    fun closeTopPanel(name: String = "default"): ChainableOperations {
        operations.add(CloseTopPanel(name))
        return this
    }
    
    fun openLeftPanel(name: String = "default"): ChainableOperations {
        operations.add(OpenLeftPanel(name))
        return this
    }
    
    fun closeLeftPanel(name: String = "default"): ChainableOperations {
        operations.add(CloseLeftPanel(name))
        return this
    }
    
    fun toChainedOperations(): ChainedOperations = ChainedOperations(operations.toList())
}
