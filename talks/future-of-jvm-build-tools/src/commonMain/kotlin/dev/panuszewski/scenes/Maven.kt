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

    val codeSample = """
        <project>
            <groupId>${groupIdFolded}...${groupIdFolded}${groupIdExpanded}pl.allegro.tech.common${groupIdExpanded}</groupId>
            <artifactId>${artifactIdFolded}...${artifactIdFolded}${artifactIdExpanded}andamio-starter-core${artifactIdExpanded}</artifactId>
            <version>${versionFolded}...${versionFolded}${versionExpanded}1.0.0${versionExpanded}</version>
            <properties>${propertiesFolded}...${propertiesFolded}${propertiesExpanded}
                <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                <kotlin.code.style>official</kotlin.code.style>
                <kotlin.compiler.jvmTarget>1.8</kotlin.compiler.jvmTarget>
            ${propertiesExpanded}</properties>
            <repositories>${repositoriesFolded}...${repositoriesFolded}${repositoriesExpanded}
                <repository>
                    <id>mavenCentral</id>
                    <url>https://repo1.maven.org/maven2/</url>
                </repository>
            ${repositoriesExpanded}</repositories>
            <dependencies>${dependenciesFolded}...${dependenciesFolded}${dependenciesExpanded}
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-core</artifactId>
                    <version>3.5.4</version>
                </dependency>
            ${dependenciesExpanded}</dependencies>
            <build>${buildFolded}...${buildFolded}${buildExpanded}
                <plugins>
                    <plugin>
                        <groupId>org.jetbrains.kotlin</groupId>
                        <artifactId>kotlin-maven-plugin</artifactId>
                        <version>2.2.0</version>
                        <executions>
                            <execution>
                                <id>compile</id>
                                <phase>compile</phase>
                                <goals>
                                    <goal>compile</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>  
                <plugins>
            ${buildExpanded}</build>
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
        .then { expandAndFocus(coordinates) }
        .then { fold(coordinates).expandAndFocus(properties) }
        .then { fold(properties).expandAndFocus(repositories) }
        .then { fold(repositories).expandAndFocus(dependencies) }
        .then { fold(dependencies).expandAndFocus(build, scroll = true) }
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
    fun expandAndFocus(scroll: Boolean = false): CodeSample {
        val expandedCodeSample = codeSample.reveal(expanded).hide(folded)
        return if (expanded.size == 1) {
            expandedCodeSample.focus(expanded.first(), scroll)
        } else {
            expandedCodeSample
        }
    }
}
