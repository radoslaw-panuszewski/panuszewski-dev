package dev.panuszewski.scenes

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.TextTag
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.CodeSample
import dev.panuszewski.template.ScrollableMagicCodeSample
import dev.panuszewski.template.buildCodeSamples
import dev.panuszewski.template.coercedGet
import dev.panuszewski.template.startWith
import dev.panuszewski.template.tag

fun StoryboardBuilder.Maven() = scene(
    stateCount = CODE_SAMPLES.size
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(16.dp))
        ProvideTextStyle(MaterialTheme.typography.h4) { Text("Maven") }
        Spacer(Modifier.height(16.dp))

        Box(Modifier.width(500.dp)) {
            transition.createChildTransition { CODE_SAMPLES.coercedGet(it.toState()) }
                .ScrollableMagicCodeSample(
                    moveDurationMillis = 500,
                    fadeDurationMillis = 500,
                    split = { it.splitByTags() }
                )
        }
    }
}

private val CODE_SAMPLES = buildCodeSamples {
    val groupIdFolded by tag()
    val groupIdExpanded by tag()
    val artifactIdFolded by tag()
    val artifactIdExpanded by tag()
    val versionFolded by tag()
    val versionExpanded by tag()
    val propertiesFolded by tag()
    val propertiesExpanded by tag()
    val repositoriesFolded by tag()
    val repositoriesExpanded by tag()
    val dependenciesFolded by tag()
    val dependenciesExpanded by tag()
    val buildFolded by tag()
    val buildExpanded by tag()

    val focusableCoordinates by tag()
    val focusableProperties by tag()
    val focusableRepositories by tag()
    val focusableDependencies by tag()
    val focusableBuild by tag()

    val codeSample = """
        <project>
            ${focusableCoordinates}<groupId>${groupIdFolded}...${groupIdFolded}${groupIdExpanded}pl.allegro.tech.common${groupIdExpanded}</groupId>
            <artifactId>${artifactIdFolded}...${artifactIdFolded}${artifactIdExpanded}andamio-starter-core${artifactIdExpanded}</artifactId>
            <version>${versionFolded}...${versionFolded}${versionExpanded}1.0.0${versionExpanded}</version>${focusableCoordinates}
            ${focusableProperties}<properties>${propertiesFolded}...${propertiesFolded}${propertiesExpanded}
                <kotlin.code.style>official</kotlin.code.style>
                <kotlin.compiler.jvmTarget>1.8</kotlin.compiler.jvmTarget>
            ${propertiesExpanded}</properties>${focusableProperties}
            ${focusableRepositories}<repositories>${repositoriesFolded}...${repositoriesFolded}${repositoriesExpanded}
                <repository>
                    <id>mavenCentral</id>
                    <url>https://repo1.maven.org/maven2/</url>
                </repository>
            ${repositoriesExpanded}</repositories>${focusableRepositories}
            ${focusableDependencies}<dependencies>${dependenciesFolded}...${dependenciesFolded}${dependenciesExpanded}
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-core</artifactId>
                    <version>3.5.4</version>
                </dependency>
            ${dependenciesExpanded}</dependencies>${focusableDependencies}
            ${focusableBuild}<build>${buildFolded}...${buildFolded}${buildExpanded}
                <plugins>
                    <plugin>
                        <groupId>org.jetbrains.kotlin</groupId>
                        <artifactId>kotlin-maven-plugin</artifactId>
                        <version>2.2.0</version>
                    </plugin>
                <plugins>
                <extensions>
                    <extension>
                        <groupId>org.apache.maven.extensions</groupId>
                        <artifactId>maven-build-cache-extension</artifactId>
                        <version>1.2.0</version>
                    </extension>
                </extensions>
            ${buildExpanded}</build>${focusableBuild}
        </project>
        """.trimIndent().toCodeSample(language = Language.Xml)

    val all = Foldable(
        folded = listOf(
            groupIdFolded,
            artifactIdFolded,
            versionFolded,
            propertiesFolded,
            repositoriesFolded,
            dependenciesFolded,
            buildFolded
        ),
        expanded = listOf(
            groupIdExpanded,
            artifactIdExpanded,
            versionExpanded,
            propertiesExpanded,
            repositoriesExpanded,
            dependenciesExpanded,
            buildExpanded
        )
    )
    val coordinates = Foldable(
        folded = listOf(groupIdFolded, artifactIdFolded, versionFolded),
        expanded = listOf(groupIdExpanded, artifactIdExpanded, versionExpanded)
    )

    val properties = Foldable(propertiesFolded, propertiesExpanded)
    val repositories = Foldable(repositoriesFolded, repositoriesExpanded)
    val dependencies = Foldable(dependenciesFolded, dependenciesExpanded)
    val build = Foldable(buildFolded, buildExpanded)

    codeSample
        .startWith { fold(all) }
        .then { expand(coordinates).focus(focusableCoordinates, scroll = false) }
        .then { fold(coordinates).expand(properties).focus(focusableProperties, scroll = false) }
        .then { fold(properties).expand(repositories).focus(focusableRepositories, scroll = false) }
        .then { fold(repositories).expand(dependencies).focus(focusableDependencies, scroll = false) }
        .then { fold(dependencies).expand(build).focus(focusableBuild, scroll = true) }
        .then { fold(build).unfocus() }
}

fun CodeSample.fold(foldable: Foldable): CodeSample = foldable.fold()
fun CodeSample.expand(foldable: Foldable): CodeSample = foldable.expand()
fun CodeSample.expandAndFocus(foldable: Foldable, scroll: Boolean = false): CodeSample = foldable.expandAndFocus(scroll)

class Foldable(
    private val folded: List<TextTag>,
    private val expanded: List<TextTag>
) {
    constructor(folded: TextTag, expanded: TextTag) :
        this(listOf(folded), listOf(expanded))

    context(codeSample: CodeSample)
    fun fold(): CodeSample =
        codeSample.reveal(folded).hide(expanded)

    context(codeSample: CodeSample)
    fun expand(): CodeSample =
        codeSample.reveal(expanded).hide(folded)

    context(codeSample: CodeSample)
    fun expandAndFocus(scroll: Boolean = false) =
        codeSample.reveal(expanded).hide(folded).focus(expanded, scroll)
}
