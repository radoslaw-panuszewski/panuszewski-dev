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
import dev.panuszewski.template.extensions.toCode
import dev.panuszewski.template.theme.LocalCodeStyle
import org.jetbrains.compose.resources.DrawableResource
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
    val warningTags: List<TextTag> = emptyList(),
    val errorTags: List<TextTag> = emptyList()
) {
    constructor(text: AnnotatedString, language: Language, title: String? = null, splitMethod: (AnnotatedString) -> List<AnnotatedString> = { it.splitByWords() })
            : this(emptyList(), FOCUSED_STYLE, UNFOCUSED_STYLE, emptyMap(), emptyMap(), null, null, text, language, title, splitMethod, emptyList())

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
            warningTags: List<TextTag> = emptyList(),
            errorTags: List<TextTag> = emptyList()
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
            warningTags = warningTags,
            errorTags = errorTags
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
        errorTags: List<TextTag> = this.errorTags,
    ): CodeSample = CodeSample(focus, focusedStyle, unfocusedStyle, replaced, styled, scrollTag, data, text, language, title, splitMethod, warningTags, errorTags)

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

    fun revealAndFocusNoStyling(vararg tags: TextTag) =
        reveal(*tags).focusNoStyling(*tags)

    fun focus(tags: List<TextTag>, scroll: Boolean = true, focusedStyle: SpanStyle? = FOCUSED_STYLE, unfocusedStyle: SpanStyle? = UNFOCUSED_STYLE): CodeSample =
        copy(focus = tags, scrollTag = if (scroll) tags.first() else scrollTag, focusedStyle = focusedStyle, unfocusedStyle = unfocusedStyle)

    fun underlineWarning(tag: TextTag): CodeSample =
        changeTagType(tag, TagType.WARNING)

    fun underlineError(tag: TextTag): CodeSample =
        changeTagType(tag, TagType.ERROR)

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

        val updatedErrorTags = when (newType) {
            TagType.ERROR -> {
                // Add tag to error tags if not already present
                if (tag in errorTags) {
                    errorTags
                } else {
                    errorTags + tag
                }
            }
            else -> {
                // Remove tag from error tags if present
                errorTags - tag
            }
        }

        return copy(warningTags = updatedWarningTags, errorTags = updatedErrorTags)
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
    
    fun changeTitle(title: String?): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(ChangeTitle(title)))
    }
    
    fun renameSelectedFile(newName: String): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(RenameSelectedFile(newName)))
    }
    
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
    
    fun revealFile(fileName: String): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(RevealFile(fileName)))
    }

    fun hideFile(fileName: String): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(HideFile(fileName)))
    }
    
    fun enlargeSelectedFile(): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(EnlargeSelectedFile))
    }
    
    fun shrinkSelectedFile(): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(ShrinkSelectedFile))
    }
    
    fun showEmoji(emoji: String): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(ShowEmoji(emoji)))
    }
    
    fun hideEmoji(): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(HideEmoji))
    }
    
    fun showImage(imageResource: DrawableResource): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(ShowImage(imageResource)))
    }
    
    fun hideImage(): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(HideImage))
    }
    
    fun openInLeftPane(fileName: String, switchTo: Boolean = false): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(OpenInLeftPane(fileName, switchTo)))
    }
    
    fun openInRightPane(fileName: String, switchTo: Boolean = false): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(OpenInRightPane(fileName, switchTo)))
    }
    
    fun openPanel(name: String): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(OpenNamedPanel(name)))
    }
    
    fun closePanel(name: String): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(CloseNamedPanel(name)))
    }

    fun openPanelAndHideFileTree(name: String): CodeSampleWithIdeOps {
        return closePanel(name).hideFileTree()
    }

    fun closePanelAndShowFileTree(name: String): CodeSampleWithIdeOps {
        return closePanel(name).showFileTree()
    }

    fun openAgenda(): CodeSampleWithIdeOps {
        return openPanelAndHideFileTree("agenda")
    }

    fun closeAgenda(): CodeSampleWithIdeOps {
        return closePanelAndShowFileTree("agenda")
    }

    fun pausePanel(name: String): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(PauseNamedPanel(name)))
    }

    fun resumePanel(name: String): CodeSampleWithIdeOps {
        return CodeSampleWithIdeOps(this, mutableListOf(ResumeNamedPanel(name)))
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
        if (errorTags != other.errorTags) return false
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
        result = 31 * result + errorTags.hashCode()
        return result
    }
}

fun <R> buildCodeSamples(builder: CodeSamplesBuilder.() -> R): R =
    CodeSamplesBuilder().builder()

data class SwitchToFile(val fileName: String)

data class ShowEmoji(val emoji: String)

object HideEmoji

data class ShowImage(val imageResource: DrawableResource)

object HideImage

data class OpenNamedPanel(val name: String)

data class CloseNamedPanel(val name: String)

data class PauseNamedPanel(val name: String)

data class ResumeNamedPanel(val name: String)

data class OpenInLeftPane(val fileName: String, val switchTo: Boolean)

data class OpenInRightPane(val fileName: String, val switchTo: Boolean)

object CloseLeftPane

object CloseRightPane

object HideFileTree

object ShowFileTree

data class RevealFile(val fileName: String)

data class HideFile(val fileName: String)

data class OpenErrorWindow(val text: String)

object CloseErrorWindow

data class ChangeTitle(val title: String?)

data class RenameSelectedFile(val newName: String)

object EnlargeSelectedFile

object ShrinkSelectedFile

data class AdvanceTogetherWith(val fileName: String)

data class ChainedOperations(val operations: List<Any>)

class CodeSampleWithIdeOps(
    val codeSample: CodeSample,
    val ideOperations: MutableList<Any>
) {
    fun changeTitle(title: String?): CodeSampleWithIdeOps {
        ideOperations.add(ChangeTitle(title))
        return this
    }
    
    fun renameSelectedFile(newName: String): CodeSampleWithIdeOps {
        ideOperations.add(RenameSelectedFile(newName))
        return this
    }
    
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
    
    fun revealFile(fileName: String): CodeSampleWithIdeOps {
        ideOperations.add(RevealFile(fileName))
        return this
    }

    fun hideFile(fileName: String): CodeSampleWithIdeOps {
        ideOperations.add(HideFile(fileName))
        return this
    }
    
    fun enlargeSelectedFile(): CodeSampleWithIdeOps {
        ideOperations.add(EnlargeSelectedFile)
        return this
    }
    
    fun shrinkSelectedFile(): CodeSampleWithIdeOps {
        ideOperations.add(ShrinkSelectedFile)
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
    
    fun showImage(imageResource: DrawableResource): CodeSampleWithIdeOps {
        ideOperations.add(ShowImage(imageResource))
        return this
    }
    
    fun hideImage(): CodeSampleWithIdeOps {
        ideOperations.add(HideImage)
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
    
    fun openNamedPanel(name: String): CodeSampleWithIdeOps {
        ideOperations.add(OpenNamedPanel(name))
        return this
    }
    
    fun closeNamedPanel(name: String): CodeSampleWithIdeOps {
        ideOperations.add(CloseNamedPanel(name))
        return this
    }

    fun pausePanel(name: String): CodeSampleWithIdeOps {
        ideOperations.add(PauseNamedPanel(name))
        return this
    }

    fun resumePanel(name: String): CodeSampleWithIdeOps {
        ideOperations.add(ResumeNamedPanel(name))
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
        val errorTags = tags.filter { it.data == TagType.ERROR }
        return CodeSample.create(extractTags(this), language, title, splitMethod, warningTags, errorTags)
    }

    fun AnnotatedString.toCodeSample(
        language: Language = Language.Kotlin,
        title: String? = null,
        splitMethod: (AnnotatedString) -> List<AnnotatedString> = { it.splitByWords() },
    ): CodeSample {
        val warningTags = tags.filter { it.data == TagType.WARNING }
        val errorTags = tags.filter { it.data == TagType.ERROR }
        return CodeSample.create(this, language, title, splitMethod, warningTags, errorTags)
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

    fun List<CodeSample>.pass(times: Int = 1): List<CodeSample> {
        var result = this
        repeat(times) {
            result = result.then { this }
        }
        return result
    }

    fun List<CodeSample>.openPanelAndHideFileTree(name: String): List<CodeSample> =
        then { openPanel(name).hideFileTree() }

    fun List<CodeSample>.closePanelAndShowFileTree(name: String): List<CodeSample> =
        then { closePanel(name).showFileTree() }

    fun List<CodeSample>.openAgenda(): List<CodeSample> =
        openPanelAndHideFileTree("agenda")

    fun List<CodeSample>.closeAgenda(): List<CodeSample> =
        closePanelAndShowFileTree("agenda")

    fun List<CodeSample>.then(transformer: CodeSample.() -> Any): List<CodeSample> {
        val lastSample = this.last()
        val cleanedSample = when (lastSample.data) {
            is SwitchToFile, is ShowEmoji, is HideEmoji, is ShowImage, is HideImage,
            is OpenInLeftPane, is OpenInRightPane,
            is CloseLeftPane, is CloseRightPane,
            is HideFileTree, is ShowFileTree, is RevealFile, is HideFile,
            is OpenErrorWindow, is CloseErrorWindow,
            is OpenNamedPanel, is CloseNamedPanel,
            is PauseNamedPanel, is ResumeNamedPanel,
            is AdvanceTogetherWith, is ChainedOperations,
            is ChangeTitle, is RenameSelectedFile, EnlargeSelectedFile, ShrinkSelectedFile -> lastSample.attach(null)
            else -> lastSample
        }

        return when (val result = transformer(cleanedSample)) {
            is CodeSampleWithIdeOps -> {
                val sampleWithChainedOps = result.codeSample.attach(ChainedOperations(result.ideOperations))
                this + sampleWithChainedOps
            }
            is CodeSample -> this + result
            else -> throw IllegalArgumentException("Transformer must return CodeSample or CodeSampleWithIdeOps")
        }
    }

    fun List<CodeSample>.thenTogetherWith(fileName: String, transformer: CodeSample.() -> Any): List<CodeSample> {
        val lastSample = this.last()
        val cleanedSample = when (lastSample.data) {
            is SwitchToFile, is ShowEmoji, is HideEmoji, is ShowImage, is HideImage,
            is OpenInLeftPane, is OpenInRightPane,
            is CloseLeftPane, is CloseRightPane,
            is HideFileTree, is ShowFileTree, is RevealFile, is HideFile,
            is OpenErrorWindow, is CloseErrorWindow,
            is OpenNamedPanel, is CloseNamedPanel,
            is PauseNamedPanel, is ResumeNamedPanel,
            is AdvanceTogetherWith, is ChainedOperations,
            is ChangeTitle, is RenameSelectedFile, EnlargeSelectedFile, ShrinkSelectedFile -> lastSample.attach(null)
            else -> lastSample
        }
        val markerSample = cleanedSample.attach(AdvanceTogetherWith(fileName))

        return when (val result = transformer(markerSample)) {
            is CodeSampleWithIdeOps -> {
                val sampleWithChainedOps = result.codeSample.attach(ChainedOperations(result.ideOperations))
                this + sampleWithChainedOps
            }
            is CodeSample -> this + result
            else -> throw IllegalArgumentException("Transformer must return CodeSample or CodeSampleWithIdeOps")
        }
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

    fun List<CodeSample>.showImage(imageResource: DrawableResource): List<CodeSample> {
        return this + last().attach(ShowImage(imageResource))
    }

    fun List<CodeSample>.hideImage(): List<CodeSample> {
        return this + last().attach(HideImage)
    }

    fun List<CodeSample>.openPanel(name: String): List<CodeSample> {
        return this + last().attach(OpenNamedPanel(name))
    }

    fun List<CodeSample>.closePanel(name: String): List<CodeSample> {
        return this + last().attach(CloseNamedPanel(name))
    }

    fun List<CodeSample>.pausePanel(name: String): List<CodeSample> {
        return this + last().attach(PauseNamedPanel(name))
    }

    fun List<CodeSample>.resumePanel(name: String): List<CodeSample> {
        return this + last().attach(ResumeNamedPanel(name))
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

    fun List<CodeSample>.revealFile(fileName: String): List<CodeSample> {
        return this + last().attach(RevealFile(fileName))
    }

    fun List<CodeSample>.hideFile(fileName: String): List<CodeSample> {
        return this + last().attach(HideFile(fileName))
    }

    fun List<CodeSample>.enlargeSelectedFile(): List<CodeSample> {
        return this + last().attach(EnlargeSelectedFile)
    }

    fun List<CodeSample>.shrinkSelectedFile(): List<CodeSample> {
        return this + last().attach(ShrinkSelectedFile)
    }

    fun List<CodeSample>.openErrorWindow(text: String): List<CodeSample> {
        return this + last().attach(OpenErrorWindow(text))
    }

    fun List<CodeSample>.closeErrorWindow(): List<CodeSample> {
        return this + last().attach(CloseErrorWindow)
    }
    
    fun List<CodeSample>.changeTitle(title: String?): List<CodeSample> {
        return this + last().attach(ChangeTitle(title))
    }
    
    fun List<CodeSample>.renameSelectedFile(newName: String): List<CodeSample> {
        return this + last().attach(RenameSelectedFile(newName))
    }

    fun List<CodeSample>.instead(transformer: CodeSample.() -> CodeSample): List<CodeSample> {
        return this.subList(fromIndex = 0, toIndex = lastIndex) + transformer(this.last())
    }
}

class ChainableOperations(val operations: MutableList<Any> = mutableListOf()) {
    fun changeTitle(title: String?): ChainableOperations {
        operations.add(ChangeTitle(title))
        return this
    }
    
    fun renameSelectedFile(newName: String): ChainableOperations {
        operations.add(RenameSelectedFile(newName))
        return this
    }
    
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
    
    fun revealFile(fileName: String): ChainableOperations {
        operations.add(RevealFile(fileName))
        return this
    }

    fun hideFile(fileName: String): ChainableOperations {
        operations.add(HideFile(fileName))
        return this
    }
    
    fun enlargeSelectedFile(): ChainableOperations {
        operations.add(EnlargeSelectedFile)
        return this
    }
    
    fun shrinkSelectedFile(): ChainableOperations {
        operations.add(ShrinkSelectedFile)
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
    
    fun openPanel(name: String): ChainableOperations {
        operations.add(OpenNamedPanel(name))
        return this
    }
    
    fun closePanel(name: String): ChainableOperations {
        operations.add(CloseNamedPanel(name))
        return this
    }

    fun pausePanel(name: String): ChainableOperations {
        operations.add(PauseNamedPanel(name))
        return this
    }

    fun resumePanel(name: String): ChainableOperations {
        operations.add(ResumeNamedPanel(name))
        return this
    }
    
    fun toChainedOperations(): ChainedOperations = ChainedOperations(operations.toList())
}
