package dev.panuszewski.template

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
    val splitMethod: (AnnotatedString) -> List<AnnotatedString>
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
    ): CodeSample = CodeSample(focus, focusedStyle, unfocusedStyle, replaced, styled, scrollTag, data, text, language, title, splitMethod)

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

    fun focus(tags: List<TextTag>, scroll: Boolean = true, focusedStyle: SpanStyle? = FOCUSED_STYLE, unfocusedStyle: SpanStyle? = UNFOCUSED_STYLE): CodeSample =
        copy(focus = tags, scrollTag = if (scroll) tags.first() else scrollTag, focusedStyle = focusedStyle, unfocusedStyle = unfocusedStyle)

    fun focus(vararg tags: TextTag, scroll: Boolean = true, focusedStyle: SpanStyle? = FOCUSED_STYLE, unfocusedStyle: SpanStyle? = UNFOCUSED_STYLE): CodeSample =
        focus(tags.asList(), scroll, focusedStyle, unfocusedStyle)

    fun focus(tag: TextTag, scroll: Boolean = true, focusedStyle: SpanStyle? = FOCUSED_STYLE, unfocusedStyle: SpanStyle? = UNFOCUSED_STYLE): CodeSample =
        focus(listOf(tag), scroll, focusedStyle, unfocusedStyle)

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CodeSample

        if (focus != other.focus) return false
        if (replaced != other.replaced) return false
        if (styled != other.styled) return false
        if (scrollTag != other.scrollTag) return false
        if (data != other.data) return false
        return true
    }

    override fun hashCode(): Int {
        var result = text.hashCode()
        result = 31 * result + (focus?.hashCode() ?: 0)
        result = 31 * result + replaced.hashCode()
        result = 31 * result + styled.hashCode()
        result = 31 * result + (scrollTag?.hashCode() ?: 0)
        result = 31 * result + (data?.hashCode() ?: 0)
        return result
    }
}

fun <R> buildCodeSamples(builder: CodeSamplesBuilder.() -> R): R =
    CodeSamplesBuilder().builder()

class CodeSamplesBuilder : TextTagScope.Default() {
    fun String.toCodeSample(
        language: Language = Language.Kotlin,
        title: String? = null,
        splitMethod: (AnnotatedString) -> List<AnnotatedString> = { it.splitByWords() },
    ): CodeSample {
        return CodeSample(extractTags(this), language, title, splitMethod)
    }

    fun AnnotatedString.toCodeSample(
        language: Language = Language.Kotlin,
        title: String? = null,
        splitMethod: (AnnotatedString) -> List<AnnotatedString> = { it.splitByWords() },
    ): CodeSample {
        return CodeSample(this, language, title, splitMethod)
    }

    fun CodeSample.collapse(data: Any?): CodeSample = collapse(tags.filter { data == it.data })
    fun CodeSample.hide(data: Any?): CodeSample = hide(tags.filter { data == it.data })
    fun CodeSample.reveal(data: Any?): CodeSample = reveal(tags.filter { data == it.data })

    fun CodeSample.then(transformer: CodeSample.() -> CodeSample): List<CodeSample> {
        return listOf(this, transformer(this))
    }

    fun List<CodeSample>.then(transformer: CodeSample.() -> CodeSample): List<CodeSample> {
        return this + transformer(this.last())
    }

    fun List<CodeSample>.instead(transformer: CodeSample.() -> CodeSample): List<CodeSample> {
        return this.subList(fromIndex = 0, toIndex = lastIndex) + transformer(this.last())
    }
}
