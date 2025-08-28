package dev.panuszewski.scenes

import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.magic.splitByWords
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.CodeSample
import dev.panuszewski.template.Foldable
import dev.panuszewski.template.MagicText
import dev.panuszewski.template.ScrollableMagicCodeSample
import dev.panuszewski.template.SlideFromLeftAnimatedVisibility
import dev.panuszewski.template.SlideFromRightAnimatedVisibility
import dev.panuszewski.template.buildCodeSamples
import dev.panuszewski.template.expand
import dev.panuszewski.template.fold
import dev.panuszewski.template.startWith
import dev.panuszewski.template.tag

private val STATE_COUNT: Int get() = BUILD_POM.size + CONSUMER_POM.size + BUILD_POM_YAML.size

fun StoryboardBuilder.Maven() = scene(
    stateCount = STATE_COUNT,
    exitTransition = { fadeOut() }
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(16.dp))
        ProvideTextStyle(MaterialTheme.typography.h4) { Text("Maven") }
        Spacer(Modifier.height(16.dp))

        Box {
            val buildPomTransition = transition.createChildTransition { plan.getSample(BUILD_POM_SLOT, it.toState()) }
            val consumerPomTransition = transition.createChildTransition { plan.getSample(CONSUMER_POM_SLOT, it.toState()) }

            val buildPomTitleTransition = transition.createChildTransition {
                if (plan.getActiveSlot(it.toState()) == CONSUMER_POM_SLOT && plan.getSample(CONSUMER_POM_SLOT, it.toState()).title == "Published pom") {
                    "Local pom"
                } else if (plan.getActiveSlot(it.toState()) == CONSUMER_POM_SLOT && plan.getSample(CONSUMER_POM_SLOT, it.toState()).title == "Consumer pom") {
                    "Build pom"
                } else {
                    plan.getSample(BUILD_POM_SLOT, it.toState()).title.orEmpty()
                }
            }
            val consumerPomTitleTransition = transition.createChildTransition { plan.getSample(CONSUMER_POM_SLOT, it.toState()).title.orEmpty() }

            val boxModifier = Modifier.width(500.dp).padding(8.dp)

            transition.SlideFromLeftAnimatedVisibility({ it.toState() >= 1 }) {
                Column(boxModifier.align(Alignment.Center).offset(x = -50.dp)) {
                    ProvideTextStyle(MaterialTheme.typography.h5) { buildPomTitleTransition.MagicText() }
                    Spacer(Modifier.height(16.dp))

                    ProvideTextStyle(MaterialTheme.typography.body1) {
                        buildPomTransition.ScrollableMagicCodeSample(
                            moveDurationMillis = 500,
                            fadeDurationMillis = 500,
                        )
                    }
                }
            }

            transition.SlideFromRightAnimatedVisibility({ plan.getActiveSlot(it.toState()) == CONSUMER_POM_SLOT }) {
                Column(boxModifier.align(Alignment.Center).offset(x = 300.dp)) {
                    ProvideTextStyle(MaterialTheme.typography.h5) { consumerPomTitleTransition.MagicText() }
                    Spacer(Modifier.height(16.dp))

                    ProvideTextStyle(MaterialTheme.typography.body1) {
                        consumerPomTransition.ScrollableMagicCodeSample(
                            moveDurationMillis = 500,
                            fadeDurationMillis = 500,
                        )
                    }
                }
            }
        }
    }
}

private val CONSUMER_POM = buildCodeSamples {
    val properties by tag()
    val build by tag()

    val codeSample = """
        <project>
            <groupId>...</groupId>
            <artifactId>...</artifactId>
            <version>...</version>${properties}
            <properties>...</properties>${properties}
            <repositories>...</repositories>
            <dependencies>...</dependencies>${build}
            <build>...</build>${build}
        </project>
        """
        .trimIndent()
        .toCodeSample(
            language = Language.Xml,
            title = "Published pom",
            splitMethod = { it.splitByTags() }
        )

    codeSample
        .then { focus(properties, build, scroll = false) }
        .then { hide(properties, build).unfocus().changeTitle("Consumer pom") }
}

private val BUILD_POM_YAML = buildCodeSamples {
    val xml by tag()
    val xmlFolded by tag()
    val xmlExpanded by tag()
    val yaml by tag()
    val toml by tag()
    val hocon by tag()

    val codeSample = """
        ${xml}<project>
            ${xmlFolded}<groupId>...</groupId>
            <artifactId>...</artifactId>
            <version>...</version>
            <dependencies>...</dependencies>
            <build>...</build>${xmlFolded}${xmlExpanded}<groupId>pl.allegro.tech.common</groupId>
            <artifactId>andamio-starter-core</artifactId>
            <version>1.0.0</version>
            <dependencies>
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-core</artifactId>
                    <version>3.5.4</version>
                </dependency>
            </dependencies>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.jetbrains.kotlin</groupId>
                        <artifactId>kotlin-maven-plugin</artifactId>
                        <version>2.2.0</version>
                    </plugin>
                <plugins>
            </build>${xmlExpanded}
        </project>${xml}${yaml}id: pl.allegro.tech.common:andamio-starter-core:1.0.0
        dependencies:
          - org.springframework.boot:spring-boot-starter-core:3.5.4
        build:
          plugins:
            - id: org.jetbrains.kotlin:kotlin-maven-plugin:2.2.0${yaml}${toml}id = "pl.allegro.tech.common:andamio-starter-core:1.0.0"
        
        dependencies = [
            "org.springframework.boot:spring-boot-starter-core:3.5.4"
        ]
        
        [build]
        [[build.plugins]]
        id = "org.jetbrains.kotlin:kotlin-maven-plugin:2.2.0"${toml}${hocon}id = "pl.allegro.tech.common:andamio-starter-core:1.0.0"
        dependencies = [
           "org.springframework.boot:spring-boot-starter-core:3.5.4"
        ]
        build {
          plugins = [
            {
              id = "org.jetbrains.kotlin:kotlin-maven-plugin:2.2.0"
            }
          ]
        }${hocon}
        """
        .trimIndent()
        .toCodeSample(
            language = Language.Xml,
            title = "pom.xml",
            splitMethod = { it.splitByWords() }
        )

    codeSample
        .startWith { reveal(xmlExpanded).hide(xmlFolded, yaml, toml, hocon) }
        .then { reveal(yaml).hide(xml).changeLanguage(Language.Yaml).changeTitle("pom.yaml") }
        .then { reveal(toml).hide(yaml).changeTitle("pom.toml") }
        .then { reveal(hocon).hide(toml).changeTitle("pom.hocon") }
}

private val BUILD_POM = buildCodeSamples {
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
        <project>${focusableCoordinates}
            <groupId>${groupIdFolded}...${groupIdFolded}${groupIdExpanded}pl.allegro.tech.common${groupIdExpanded}</groupId>
            <artifactId>${artifactIdFolded}...${artifactIdFolded}${artifactIdExpanded}andamio-starter-core${artifactIdExpanded}</artifactId>
            <version>${versionFolded}...${versionFolded}${versionExpanded}1.0.0${versionExpanded}</version>${focusableCoordinates}${focusableProperties}
            <properties>${propertiesFolded}...${propertiesFolded}${propertiesExpanded}
                <kotlin.code.style>official</kotlin.code.style>
                <kotlin.compiler.jvmTarget>1.8</kotlin.compiler.jvmTarget>
            ${propertiesExpanded}</properties>${focusableProperties}${focusableRepositories}
            <repositories>${repositoriesFolded}...${repositoriesFolded}${repositoriesExpanded}
                <repository>
                    <id>mavenCentral</id>
                    <url>https://repo1.maven.org/maven2/</url>
                </repository>
            ${repositoriesExpanded}</repositories>${focusableRepositories}${focusableDependencies}
            <dependencies>${dependenciesFolded}...${dependenciesFolded}${dependenciesExpanded}
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-core</artifactId>
                    <version>3.5.4</version>
                </dependency>
            ${dependenciesExpanded}</dependencies>${focusableDependencies}${focusableBuild}
            <build>${buildFolded}...${buildFolded}${buildExpanded}
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
        """
        .trimIndent()
        .toCodeSample(
            language = Language.Xml,
            title = "pom.xml",
            splitMethod = { it.splitByTags() }
        )

    val all = Foldable(
        folded = listOf(groupIdFolded, artifactIdFolded, versionFolded, propertiesFolded, repositoriesFolded, dependenciesFolded, buildFolded),
        expanded = listOf(groupIdExpanded, artifactIdExpanded, versionExpanded, propertiesExpanded, repositoriesExpanded, dependenciesExpanded, buildExpanded)
    )
    val coordinates = Foldable(
        folded = listOf(groupIdFolded, artifactIdFolded, versionFolded), expanded = listOf(groupIdExpanded, artifactIdExpanded, versionExpanded)
    )

    codeSample
        .startWith { fold(all) }
        .then { this }
        .then { expand(all) }
        .then { fold(all) }
}

class CodeSamplesPlan {
    private val slots = mutableSetOf<String>()
    private val revelations = mutableListOf<Pair<String, List<CodeSample>>>()
    private val samples = mutableMapOf<String, MutableList<CodeSample>>()

    /**
     * Register a slot that will hold code samples.
     * Only one slot at a time will be revealing subsequent code samples
     */
    fun registerSlot(slotName: String) {
        slots.add(slotName)
        samples[slotName] = mutableListOf()
    }

    /**
     * Schedule code samples to be revealed in the slot.
     * The order of invoking this method matters. For example:
     * ```kotlin
     * plan.thenRevealInSlot("A", firstSamplesA)
     * plan.thenRevealInSlot("B", firstSamplesB)
     * plan.thenRevealInSlot("A", secondSamplesA)
     * ```
     * In the above example:
     * - first slot A is activated, and it will reveal all samples from the `firstSamplesA` list
     * - then slot B is activated, and it will reveal all samples from the `firstSamplesB` list
     * - then slot A is activated again, and it will reveal all samples from the `secondSamplesA` list
     */
    fun thenRevealInSlot(slotName: String, codeSamples: List<CodeSample>) {
        require(slotName in slots) { "Slot $slotName not registered" }
        revelations.add(slotName to codeSamples)
        samples[slotName]!!.addAll(codeSamples)
    }

    /**
     * Helper method to calculate the active revelation index and remaining state for a given state.
     * Returns a Pair of (activeRevelationIndex, remainingState).
     */
    private fun calculateActiveRevelation(state: Int): Pair<Int, Int> {
        require(revelations.isNotEmpty()) { "No revelations registered" }

        var remainingState = state

        // Find the active revelation based on the state
        for (i in revelations.indices) {
            val (_, samples) = revelations[i]
            if (remainingState < samples.size) {
                return i to remainingState
            }
            remainingState -= samples.size
        }

        // If we've gone through all revelations, return the last one
        return revelations.lastIndex to 0
    }

    /**
     * Returns the active slot name for the given state.
     * The active slot is the one that is currently revealing samples.
     */
    fun getActiveSlot(state: Int): String {
        val (activeRevelationIndex, _) = calculateActiveRevelation(state)
        return revelations[activeRevelationIndex].first
    }

    /**
     * Get the sample index for the active slot at the given state and revelation index.
     */
    private fun getActiveSlotSampleIndex(slotName: String, activeRevelationIndex: Int, remainingState: Int): Int {
        var sampleIndex = 0

        // Count samples in previous revelations for this slot
        for (i in 0 until activeRevelationIndex) {
            val (slot, slotSamples) = revelations[i]
            if (slot == slotName) {
                sampleIndex += slotSamples.size
            }
        }

        // Add the remaining state to get the final index
        return sampleIndex + remainingState
    }

    /**
     * Get the sample index for an inactive slot at the given state.
     */
    private fun getInactiveSlotSampleIndex(slotName: String, state: Int): Int {
        var lastRevealedIndex = -1
        var samplesProcessed = 0

        for (i in revelations.indices) {
            val (slot, slotSamples) = revelations[i]

            // If we've processed more samples than the current state, we're done
            if (samplesProcessed > state) {
                break
            }

            // If this revelation is for the requested slot, update the last revealed index
            if (slot == slotName) {
                // If this revelation would be partially revealed, calculate the exact index
                if (samplesProcessed + slotSamples.size > state) {
                    val partialIndex = state - samplesProcessed
                    lastRevealedIndex = lastRevealedIndex + partialIndex + 1
                } else {
                    // Otherwise, all samples in this revelation are revealed
                    lastRevealedIndex += slotSamples.size
                }
            }

            samplesProcessed += slotSamples.size
        }

        // If no samples have been revealed yet, return 0 (first sample)
        return if (lastRevealedIndex <= 0) 0 else lastRevealedIndex.coerceAtMost(samples[slotName]!!.size - 1)
    }

    /**
     * For an active slot, calling this method with subsequent states will return subsequent code samples.
     * For an inactive slot, calling this method with subsequent states will return the same code sample.
     */
    fun getSample(slotName: String, state: Int): CodeSample {
        require(slotName in slots) { "Slot $slotName not registered" }
        require(samples[slotName]!!.isNotEmpty()) { "No samples in slot $slotName" }

        // Get the active slot for the current state
        val activeSlot = getActiveSlot(state)

        // Calculate the active revelation index and remaining state
        val (activeRevelationIndex, remainingState) = calculateActiveRevelation(state)

        // Calculate the sample index based on whether the slot is active or inactive
        val sampleIndex = if (slotName == activeSlot) {
            getActiveSlotSampleIndex(slotName, activeRevelationIndex, remainingState)
        } else {
            getInactiveSlotSampleIndex(slotName, state)
        }

        return samples[slotName]!![sampleIndex]
    }
}

const val BUILD_POM_SLOT = "BUILD_POM"
const val CONSUMER_POM_SLOT = "CONSUMER_POM"

val plan = CodeSamplesPlan().apply {
    registerSlot(BUILD_POM_SLOT)
    registerSlot(CONSUMER_POM_SLOT)

    thenRevealInSlot(BUILD_POM_SLOT, BUILD_POM)
    thenRevealInSlot(CONSUMER_POM_SLOT, CONSUMER_POM)
    thenRevealInSlot(BUILD_POM_SLOT, BUILD_POM_YAML)
}

