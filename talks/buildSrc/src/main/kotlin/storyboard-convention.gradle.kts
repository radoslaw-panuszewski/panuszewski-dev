@file:OptIn(ExperimentalKotlinGradlePluginApi::class)
@file:Suppress("OPT_IN_USAGE")

import gradle.kotlin.dsl.accessors._02ddedda7cdb49b36a1e241175b7e9c6.javaToolchains
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("compose-convention")
}

kotlin {
    jvm {
        binaries {
            executable {
                mainClass = "dev.panuszewski.JvmMainKt"
            }
        }
    }

//    wasmJs {
//        binaries.executable()
//        browser {
//            commonWebpackConfig {
//                outputFileName = "storyboard.js"
//            }
//        }
//    }
}

tasks {
    named<Zip>("jvmDistZip") {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    named<Tar>("jvmDistTar") {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    named<Sync>("installJvmDist") {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    named<JavaExec>("jvmRunHot") {
        mainClass = "dev.panuszewski.JvmMainKt"
        javaLauncher = javaToolchains.launcherFor { languageVersion = JavaLanguageVersion.of(25) }
    }
}
