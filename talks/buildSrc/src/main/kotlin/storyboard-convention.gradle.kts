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