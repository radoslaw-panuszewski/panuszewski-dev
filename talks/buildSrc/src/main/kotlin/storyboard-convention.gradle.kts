@file:OptIn(ExperimentalKotlinGradlePluginApi::class)
@file:Suppress("OPT_IN_USAGE")

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

    wasmJs {
        binaries.executable()
        browser {
            commonWebpackConfig {
                outputFileName = "storyboard.js"
            }
        }
    }
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
}