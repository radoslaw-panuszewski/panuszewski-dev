package dev.panuszewski.scenes

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.components.IDE
import dev.panuszewski.template.components.IdeState
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.components.SlidingTitleScaffold
import dev.panuszewski.template.components.addFile
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.extensions.ComposableLambda
import dev.panuszewski.template.extensions.precompose
import dev.panuszewski.template.extensions.sortedMapOf
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.subsequentNumbers
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withStateTransition
import dev.panuszewski.template.theme.BULLET_1

fun StoryboardBuilder.WhyBother() {

    val ideAppears = 2

    val (
        ideScrolls,
        ideShrinks,
        bulletpointsAppear
    ) =
        subsequentNumbers(since = ideAppears + 1)

    val (
        imperativeCodeBulletpoint,
        crossConfigurationBulletpoint,
        mixedConcernsBulletpoint,
        noTypeSafetyBulletpoint,
        groovyBulletpoint,
    ) =
        subsequentNumbers(since = bulletpointsAppear)

    scene(stateCount = 100) {
        withStateTransition {
            BUILD_GRADLE_KTS.precompose()

            SlidingTitleScaffold("Why bother?") {
                val files = buildList {
                    addFile(
                        name = "build.gradle",
                        content = createChildTransition {
                            when {
                                it == ideScrolls -> BUILD_GRADLE_KTS[6]
                                it == mixedConcernsBulletpoint -> BUILD_GRADLE_KTS[5]
                                it == crossConfigurationBulletpoint -> BUILD_GRADLE_KTS[4]
                                it == imperativeCodeBulletpoint -> BUILD_GRADLE_KTS[3]
                                it == noTypeSafetyBulletpoint -> BUILD_GRADLE_KTS[2]
                                it == groovyBulletpoint -> BUILD_GRADLE_KTS[1]
                                else -> BUILD_GRADLE_KTS[0]
                            }
                        }
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 32.dp).align(TopStart),
                ) {
                    RevealSequentially(since = bulletpointsAppear) {
                        val bulletpoints = sortedMapOf<Int, ComposableLambda>(
                            noTypeSafetyBulletpoint to { Text("$BULLET_1 No type safety") },
                            imperativeCodeBulletpoint to { Text("$BULLET_1 Imperative code") },
                            crossConfigurationBulletpoint to { Text("$BULLET_1 Cross configuration") },
                            mixedConcernsBulletpoint to { Text("$BULLET_1 Mixed concerns") },
                            groovyBulletpoint to { Text("$BULLET_1 Groovy 🤢") },
                        )
                        bulletpoints.forEach { item(content = it.value) }
                    }
                }

                val ideStartPadding by animateDp {
                    when {
                        it >= ideShrinks -> 260.dp
                        else -> 0.dp
                    }
                }

                val fileTreeWidth by animateDp {
                    when {
                        it >= ideShrinks -> 0.dp
                        else -> 275.dp
                    }
                }

                IDE(
                    IdeState(
                        files = files,
                        selectedFile = "build.gradle",
                        fileTreeHidden = currentState >= ideShrinks,
                    ),
                    modifier = Modifier.padding(start = ideStartPadding),
                    fileTreeWidth = fileTreeWidth,
                )
            }
        }
    }
}

private val BUILD_GRADLE_KTS = buildCodeSamples {
    val allCode by tag()
    val noTypesafe1 by tag()
    val noTypesafe2 by tag()
    val noTypesafe3 by tag()
    val imperative1 by tag()
    val imperative2 by tag()
    val imperative3 by tag()
    val crossConfig1 by tag()
    val crossConfig2 by tag()
    val mixedConcerns by tag()
    val bottom by tag()

    """
    ${allCode}buildscript {
        classpath(${noTypesafe1}'org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20'${noTypesafe1})
    }
    
    ${crossConfig1}allprojects${crossConfig1} {
        ${imperative1}apply plugin: 'kotlin'${imperative1}
    }
        
    ${crossConfig2}subprojects${crossConfig2}
        ${imperative2}.findAll { it.name.endsWith('-library') }
        .forEach { it.apply plugin: 'java-library' }${imperative2}
    
    ${mixedConcerns}dependencies {
        implementation ${noTypesafe2}project(':first-library')${noTypesafe2}
    }
    
    tasks.register('sayHello') {
        doLast {
            ${imperative3}println 'lol'${imperative3}
        }
    }${mixedConcerns}${allCode}
    
    
    def random = new Random((buildscript.ext.weirdNumber as long) * 1337)

    def glitch = { key, fallback ->
        def env = System.getenv(key as String)
        return env?.trim() ?: (project.findProperty(key) ?: fallback)
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
        dynamicVersion = { base -> "${'$'}{'$'}{base}.${'$'}{'$'}{random.nextInt(10)}.${'$'}{'$'}{(System.currentTimeMillis() % 1000)}" }
        computedGroup = (glitch('GROUP','com.example') + "." + nonsense.colors[random.nextInt(nonsense.colors.size())]).toLowerCase()
    }
    
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
        implementation platform("org.springframework.boot:spring-boot-dependencies:${'$'}{'$'}{glitch('SBOM','3.3.3')}")
        implementation 'org.apache.commons:commons-lang3:3.15.0'
        implementation "com.google.guava:guava:${'$'}{'$'}{['33.3.1-jre','32.1.3-jre','31.1-jre'][random.nextInt(3)]}"
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
            println "group      : ${'$'}{'$'}{project.group}"
            println "version    : ${'$'}{'$'}{project.version}"
            println "java       : ${'$'}{'$'}{java.toolchain.languageVersion}"
            println "colors     : ${'$'}{'$'}{ext.nonsense.colors}"
            println "answer     : ${'$'}{'$'}{ext.nonsense.answer}"
            println "flip()     : ${'$'}{'$'}{ext.nonsense.flip()}"
            println "chaos      : ${'$'}{'$'}{ext.nonsense.chaos}"
            println "computed   : ${'$'}{'$'}{ext.dynamicVersion('0.0')}"
            println "repos      : ${'$'}{'$'}{repositories.collect{it.class.simpleName}}"
        }
    }
    
    abstract class EchoTask extends DefaultTask {
        @Input String message = '...'
        @TaskAction void speak() { println "[Echo] ${'$'}{'$'}message" }
    }

    (1..(3 + random.nextInt(3))).each { idx ->
        tasks.register("echo${'$'}{'$'}{idx}", EchoTask) { t ->
            t.group = 'echo'
            t.message = "Echo number ${'$'}{'$'}{idx} @ ${'$'}{'$'}{new Date()}"
        }
    }

    ['alpha','beta','gamma','delta'].each { n ->
        tasks.register("mangle_${'$'}{'$'}{n}") {
            dependsOn tasks.withType(EchoTask)
            doFirst { println "[mangle:${'$'}{'$'}{n}] first @ ${'$'}{'$'}{System.nanoTime()}" }
            doLast {
                def r = (0..<random.nextInt(7)+1).collect { UUID.randomUUID().toString().substring(0,8)}
                println "[mangle:${'$'}{'$'}{n}] data => ${'$'}{'$'}{r}"
            }
        }
    }

    if (ext.nonsense.flip()) {
        apply plugin: 'com.github.johnrengelman.shadow'
        tasks.register('uberJar', Jar) {
            dependsOn tasks.named('shadowJar')
            archiveClassifier = 'uber'
            from { zipTree(tasks.named('shadowJar').get().archiveFile.get()) }
        }
    }

    Test testTask = (tasks.named('test').get() as Test)
    testTask.useJUnitPlatform()
    testTask.systemProperty 'file.encoding', 'UTF-8'
    testTask.systemProperty 'chaos', ext.nonsense.chaos

    tasks.register('flipCoin') {
        doLast { println "Heads? ${'$'}{'$'}{ext.nonsense.flip()}" }
    }

    ['build','check','assemble'].each { nm ->
        tasks.named(nm).configure { t ->
            dependsOn tasks.withType(EchoTask)
            finalizedBy 'printState'
        }
    }

    ext.metaClass.dynamic = 'enabled'
    ext.metaClass.methodMissing = { String name, args ->
        println "You called ${'$'}{'$'}{name} with ${'$'}{'$'}{args?.toList()} for no good reason."
    }
    project.metaClass.getObscure = { String k ->
        return [k, k.reverse()].join(':')
    }

    afterEvaluate {
        if (project.hasProperty('enableExtra') || ext.nonsense.flip()) {
            dependencies { implementation 'org.yaml:snakeyaml:2.2' }
            println "[afterEvaluate] added snakeyaml because vibes"
        }
    }

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
                url = uri("${'$'}{'$'}{buildDir}/repo/${'$'}{'$'}{ext.nonsense.chaos}")
            }
        }
    }

    signing {
        required { gradle.taskGraph.hasTask('publish') && ext.nonsense.flip() }
        useInMemoryPgpKeys(glitch('GPG_KEY',''), glitch('GPG_PASS',''))
        sign publishing.publications
    }

    gradle.taskGraph.whenReady { graph ->
        println "Task graph has ${'$'}{'$'}{graph.allTasks.size()} tasks. Version=${'$'}{'$'}{project.version}"
    }

    tasks.register('leakProps') {
        doLast {
            println "PROPS:"; project.properties.keySet().sort().each { println " - ${'$'}{'$'}it" }
        }
    }

    configurations {
        dumpster { transitive = false }
    }

    tasks.register('dumpsterDive', Copy) {
        from configurations.runtimeClasspath
        into layout.buildDirectory.dir("dumpster/${'$'}{'$'}{System.currentTimeMillis()}")
        include { details -> details.file.name.endsWith('.jar') }
    }

    def generatedDir = layout.buildDirectory.dir('generated/src').get().asFile

    tasks.register('generateGarbage') {
        outputs.dir generatedDir
        doLast {
            def pkg = "p${'$'}{'$'}{random.nextInt(100)}.q${'$'}{'$'}{random.nextInt(100)}.r${'$'}{'$'}{random.nextInt(100)}"
            def f = new File(generatedDir, "${'$'}{'$'}{pkg.replace('.','/')}/Noise${'$'}{'$'}{random.nextInt(999)}.java")
            f.parentFile.mkdirs()
            f.text = ""${'$'}{'"'}
            package ${'$'}{'$'}{pkg};
            public class Noise${'$'}{'$'}{random.nextInt(999)} { public static String say(){ return \"${'$'}{'$'}{UUID.randomUUID()}\"; } }
                ""${'$'}{'"'}.stripIndent()
                println "Generated: ${'$'}{'$'}{f}"
            }
            }
    ${bottom} ${bottom}
            compileJava.dependsOn tasks.named('generateGarbage')

            idea {
                module { inheritOutputDirs = false; outputDir file("${'$'}{'$'}{buildDir}/idea-out") }
            }

            eclipse {
                classpath { plusConfigurations += [configurations.jank] }
            }

            ['mangle_alpha','mangle_beta','mangle_gamma','mangle_delta'].collate(2).each { pair ->
                tasks.named(pair[0]).configure { finalizedBy pair[1] }
            }

            tasks.register('yell') {
                doFirst { println 'WHY ARE WE SHOUTING' }
                doLast { println 'BECAUSE CONFIG IS LOUD' }
            }

            def sillyBanner = (0..<random.nextInt(10)+5).collect { '*' }.join('')
    """
        .trimIndent()
        .toCodeSample(language = Language.Groovy)
        .startWith { this }
        .then { underline(allCode) }
        .then { resetUnderline(allCode).focus(noTypesafe1, noTypesafe2, noTypesafe3) }
        .then { focus(imperative1, imperative2, imperative3) }
        .then { focus(crossConfig1, crossConfig2) }
        .then { focus(mixedConcerns, scroll = false) }
        .then { focus(bottom, focusedStyle = null, unfocusedStyle = null) }
}