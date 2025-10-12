package dev.panuszewski.scenes

import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.FadeOutAnimatedVisibility
import dev.panuszewski.template.extensions.Text
import dev.panuszewski.template.extensions.h3
import dev.panuszewski.template.extensions.subsequentNumbers
import dev.panuszewski.template.extensions.withIntTransition

fun StoryboardBuilder.TitleScene() {
    val (
        titleDisappears,
        chaoticConfigAppears,
        chaoticConfigScrollsToBottom
    ) = subsequentNumbers(since = 1)

    scene(100) {
        withIntTransition {
            FadeOutAnimatedVisibility({ it < titleDisappears }) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    h3 {
                        Text {
                            append("Purging the ")
                            withStyle(SpanStyle(color = Color(0xFF682F29), fontWeight = Medium)) { append("Chaos") }
                        }
                        Text("from Your Gradle Build")
                    }
                }
            }

            FadeInOutAnimatedVisibility({ it >= chaoticConfigAppears }) {

                val scrollPosition by animateInt({ tween(5000) }) {
                    when {
                        it >= chaoticConfigScrollsToBottom -> 10000
                        else -> 0
                    }
                }

                ProvideTextStyle(
                    TextStyle(
                        color = Color.Gray,
                        fontSize = 5.sp,
                        lineHeight = 5.sp
                    )
                ) {
                    Text(
                        text = CHAOTIC_GRADLE_CONFIG,
                        modifier = Modifier.padding(32.dp).verticalScroll(ScrollState(scrollPosition))
                    )
                }
            }
        }
    }
}

private val CHAOTIC_GRADLE_CONFIG = $$"""
    buildscript {
        ext.set('weirdNumber', (System.currentTimeMillis() % 97) + 13)
        ext.globalToggle = System.getenv('GLOBAL_TOGGLE') ?: 'maybe'

        repositories {
            mavenLocal()
            mavenCentral()
            google()
            maven { url = uri("https://plugins.gradle.org/m2/") }
            maven { url = uri("https://repo1.maven.org/maven2") }
            maven { url = uri("https://jitpack.io") }
            flatDir { dirs "libs", "../shared-libs" }
        }

        dependencies {
            classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${System.getenv('KOTLIN_VERSION') ?: '2.0.20'}")
            classpath 'com.android.tools.build:gradle:8.7.0'
            classpath "io.github.gradle-nexus:publish-plugin:2.0.0"
            classpath("gradle.plugin.com.github.johnrengelman:shadow:8.1.1")
            classpath "org.ajoberstar:grgit:5.2.2"
        }
    }

    def random = new Random((buildscript.ext.weirdNumber as long) * 1337)

    def glitch = { key, fallback ->
        def env = System.getenv(key as String)
        return env?.trim() ?: (project.findProperty(key) ?: fallback)
    }

    plugins {
        id 'java'
        id 'groovy'
        id 'maven-publish'
        id 'signing'
        id 'jacoco'
        id 'checkstyle'
        id 'pmd'
        id 'com.github.johnrengelman.shadow' apply false
    }

    apply plugin: 'idea'
    apply plugin: 'eclipse'

    ext {
        nonsense = [
            colors: ['cerulean','puce','chartreuse','smalt'],
            answer: 42 + random.nextInt(7) - random.nextInt(6),
            flip  : { -> [true,false,false,true][random.nextInt(4)] },
            chaos : UUID.randomUUID().toString().replace('-',''),
        ]
        dynamicVersion = { base -> "${base}.${random.nextInt(10)}.${(System.currentTimeMillis() % 1000)}" }
        computedGroup = (glitch('GROUP','com.example') + "." + nonsense.colors[random.nextInt(nonsense.colors.size())]).toLowerCase()
    }

    group = ext.computedGroup
    version = (glitch('VERSION', "1.${random.nextInt(100)}.${random.nextInt(1000)}-${ext.nonsense.chaos.substring(0,6)}"))

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(glitch('JAVA_VERSION','17') as int)
        }
        withSourcesJar()
        withJavadocJar()
    }

    repositories {
        mavenLocal()
        mavenCentral()
        if (ext.nonsense.flip()) {
            maven { url = uri('https://repo.maven.apache.org/maven2') }
        } else {
            maven { url = uri('https://maven-central.storage-download.googleapis.com/maven2') }
        }
        exclusiveContent {
            forRepository { maven { url = uri('https://jitpack.io') } }
            filter { includeGroupByRegex("com\\.github\\..*") }
        }
    }

    configurations {
        jank
        apiElements.extendsFrom(jank)
        implementation { canBeResolved = true; canBeConsumed = true }
        chaos { transitive = false }
        all { resolutionStrategy.cacheDynamicVersionsFor 5, 'minutes' }
    }

    sourceSets {
        main {
            java.srcDirs += ['src/convoluted/java', 'build/generated/src']
            resources.srcDirs += ['src/confusing/resources']
        }
        test {
            java.srcDir 'src/messyTest/java'
            resources.srcDir 'src/messyTest/resources'
        }
        create('extra') {
            java.srcDir 'src/extra/java'
            compileClasspath += sourceSets.main.output
            runtimeClasspath += output + sourceSets.main.output
        }
    }

    dependencies {
        implementation platform("org.springframework.boot:spring-boot-dependencies:${glitch('SBOM','3.3.3')}")
        implementation 'org.apache.commons:commons-lang3:3.15.0'
        implementation "com.google.guava:guava:${['33.3.1-jre','32.1.3-jre','31.1-jre'][random.nextInt(3)]}"
        compileOnly 'org.projectlombok:lombok:1.18.34'
        annotationProcessor 'org.projectlombok:lombok:1.18.34'
        testImplementation 'org.junit.jupiter:junit-jupiter:5.11.1'
        testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
        jank files('libs/manual.jar')
        chaos 'com.github.user:nonexistent:0.0.0-REALLY-DONT-RESOLVE'
        extraImplementation sourceSets.main.output
    }

    configurations.all {
        resolutionStrategy {
            def forced = [
                'org.slf4j:slf4j-api:2.0.16',
                'commons-io:commons-io:2.16.1'
            ]
            force forced as String[]
            eachDependency { details ->
                if (details.requested.group == 'com.google.guava' && details.requested.version?.endsWith('-jre')) {
                    details.useVersion(details.requested.version)
                }
                if (details.requested.name == 'groovy') {
                    details.because('why not')
                }
            }
            componentSelection {
                all { ComponentSelection selection ->
                    if (selection.candidate.version?.contains('rc') || selection.candidate.version?.contains('alpha')) {
                        selection.reject('No pre-releases today, maybe tomorrow')
                    }
                }
            }
        }
    }

    checkstyle {
        toolVersion = '10.18.2'
        configProperties = [severity: 'ignore', maxWarnings: Integer.MAX_VALUE]
    }

    pmd {
        consoleOutput = ext.nonsense.flip()
        ruleSets = []
    }

    jacoco {
        toolVersion = '0.8.12'
    }

    tasks.register('printState') {
        group = 'diagnostics'
        description = 'Prints questionable internal state.'
        doLast {
            println "group      : ${project.group}"
            println "version    : ${project.version}"
            println "java       : ${java.toolchain.languageVersion}"
            println "colors     : ${ext.nonsense.colors}"
            println "answer     : ${ext.nonsense.answer}"
            println "flip()     : ${ext.nonsense.flip()}"
            println "chaos      : ${ext.nonsense.chaos}"
            println "computed   : ${ext.dynamicVersion('0.0')}"
            println "repos      : ${repositories.collect{it.class.simpleName}}"
        }
    }

    abstract class EchoTask extends DefaultTask {
        @Input String message = '...'
        @TaskAction void speak() { println "[Echo] $message" }
    }

    (1..(3 + random.nextInt(3))).each { idx ->
        tasks.register("echo${idx}", EchoTask) { t ->
            t.group = 'echo'
            t.message = "Echo number ${idx} @ ${new Date()}"
        }
    }

    // Tasks that do odd things at odd times
    ['alpha','beta','gamma','delta'].each { n ->
        tasks.register("mangle_${n}") {
            dependsOn tasks.withType(EchoTask)
            doFirst { println "[mangle:${n}] first @ ${System.nanoTime()}" }
            doLast {
                def r = (0..<random.nextInt(7)+1).collect { UUID.randomUUID().toString().substring(0,8)}
                println "[mangle:${n}] data => ${r}"
            }
        }
    }

    // Shadow fat jar toggled by coin flip
    if (ext.nonsense.flip()) {
        apply plugin: 'com.github.johnrengelman.shadow'
        tasks.register('uberJar', Jar) {
            dependsOn tasks.named('shadowJar')
            archiveClassifier = 'uber'
            from { zipTree(tasks.named('shadowJar').get().archiveFile.get()) }
        }
    }

    // Wire up test platform with needless indirection
    Test testTask = (tasks.named('test').get() as Test)
    testTask.useJUnitPlatform()
    testTask.systemProperty 'file.encoding', 'UTF-8'
    testTask.systemProperty 'chaos', ext.nonsense.chaos

    tasks.register('flipCoin') {
        doLast { println "Heads? ${ext.nonsense.flip()}" }
    }

    // random task dependencies
    ['build','check','assemble'].each { nm ->
        tasks.named(nm).configure { t ->
            dependsOn tasks.withType(EchoTask)
            finalizedBy 'printState'
        }
    }

    // Ugly dynamic properties & methodMissing
    ext.metaClass.dynamic = 'enabled'
    ext.metaClass.methodMissing = { String name, args ->
        println "You called ${name} with ${args?.toList()} for no good reason."
    }
    project.metaClass.getObscure = { String k ->
        return [k, k.reverse()].join(':')
    }

    // afterEvaluate nonsense
    afterEvaluate {
        if (project.hasProperty('enableExtra') || ext.nonsense.flip()) {
            dependencies { implementation 'org.yaml:snakeyaml:2.2' }
            println "[afterEvaluate] added snakeyaml because vibes"
        }
    }

    // Publications with shaky configuration
    publishing {
        publications {
            create('mavenJava', MavenPublication) {
                from components.java
                groupId = project.group.toString()
                artifactId = (project.name + '-' + ext.nonsense.colors[random.nextInt(ext.nonsense.colors.size())]).toLowerCase()
                version = project.version.toString()
                pom {
                    name = artifactId
                    description = 'No one knows what this does.'
                    url = 'https://example.invalid/' + artifactId
                    licenses { license { name = 'Unlicense'; url = 'https://unlicense.org/' } }
                    scm { url = 'https://nowhere.invalid/repo.git' }
                    developers { developer { id = 'ghost'; name = 'Anonymous Specter' } }
                }
            }
        }
        repositories {
            maven {
                name = 'localChaos'
                url = uri("${buildDir}/repo/${ext.nonsense.chaos}")
            }
        }
    }

    signing {
        required { gradle.taskGraph.hasTask('publish') && ext.nonsense.flip() }
        useInMemoryPgpKeys(glitch('GPG_KEY',''), glitch('GPG_PASS',''))
        sign publishing.publications
    }

    // Task graph mischief
    gradle.taskGraph.whenReady { graph ->
        println "Task graph has ${graph.allTasks.size()} tasks. Version=${project.version}"
    }

    tasks.register('leakProps') {
        doLast {
            println "PROPS:"; project.properties.keySet().sort().each { println " - $it" }
        }
    }

    // grotesque custom configuration copy
    configurations {
        dumpster { transitive = false }
    }

    tasks.register('dumpsterDive', Copy) {
        from configurations.runtimeClasspath
        into layout.buildDirectory.dir("dumpster/${System.currentTimeMillis()}")
        include { details -> details.file.name.endsWith('.jar') }
    }

    // generate fake sources just to annoy people
    def generatedDir = layout.buildDirectory.dir('generated/src').get().asFile

    tasks.register('generateGarbage') {
        outputs.dir generatedDir
        doLast {
            def pkg = "p${random.nextInt(100)}.q${random.nextInt(100)}.r${random.nextInt(100)}"
            def f = new File(generatedDir, "${pkg.replace('.','/')}/Noise${random.nextInt(999)}.java")
            f.parentFile.mkdirs()
            f.text = ""${'"'}
                package ${pkg};
                public class Noise${random.nextInt(999)} { public static String say(){ return \"${UUID.randomUUID()}\"; } }
            ""${'"'}.stripIndent()
            println "Generated: ${f}"
        }
    }

    compileJava.dependsOn tasks.named('generateGarbage')

    // idea & eclipse tweaks for no reason
    idea {
        module { inheritOutputDirs = false; outputDir file("${buildDir}/idea-out") }
    }

    eclipse {
        classpath { plusConfigurations += [configurations.jank] }
    }

    // lifecycle tasks chaining in circles
    ['mangle_alpha','mangle_beta','mangle_gamma','mangle_delta'].collate(2).each { pair ->
        tasks.named(pair[0]).configure { finalizedBy pair[1] }
    }

    tasks.register('yell') {
        doFirst { println 'WHY ARE WE SHOUTING' }
        doLast { println 'BECAUSE CONFIG IS LOUD' }
    }

    def sillyBanner = (0..<random.nextInt(10)+5).collect { '*' }.join('')
    println "${sillyBanner} BUILD INGESTED ${sillyBanner}"

"""
    .trimIndent()
