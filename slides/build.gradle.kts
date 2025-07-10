@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    kotlin("plugin.compose")
    id("org.jetbrains.compose")
    id("org.jetbrains.compose.hot-reload")
}

group = "dev.panuszewski"
version = "0.1.0-SNAPSHOT"

kotlin {
    jvm {
        binaries {
            executable {
                mainClass = "dev.panuszewski.Main_desktopKt"
            }
        }
    }
    jvmToolchain(21)

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        binaries.executable()
        browser()
    }

    sourceSets {
        all {
            languageSettings {
                enableLanguageFeature("ContextParameters")
                enableLanguageFeature("WhenGuards")
                enableLanguageFeature("MultiDollarInterpolation")

                optIn("androidx.compose.animation.core.ExperimentalTransitionApi")
                optIn("androidx.compose.animation.ExperimentalAnimationApi")
                optIn("androidx.compose.animation.ExperimentalSharedTransitionApi")
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }

        commonMain {
            dependencies {
                implementation("dev.bnorm.storyboard:storyboard:0.1.0-alpha01")
                implementation("dev.bnorm.storyboard:storyboard-easel:0.1.0-alpha01")
                implementation("dev.bnorm.storyboard:storyboard-text:0.1.0-alpha01")
                implementation("dev.chrisbanes.haze:haze:1.5.2")

                implementation(compose.material)
                implementation(compose.components.resources)
            }
        }
        jvmMain {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}
