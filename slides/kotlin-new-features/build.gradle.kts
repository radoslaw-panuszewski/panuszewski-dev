@file:OptIn(ExperimentalKotlinGradlePluginApi::class)
@file:Suppress("OPT_IN_USAGE")

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jetbrains.compose.hot.reload)
}

kotlin {
    jvmToolchain(24)

    jvm {
        binaries {
            executable {
                mainClass = "dev.panuszewski.Main_desktopKt"
            }
        }
    }

    wasmJs {
        binaries.executable()
        browser {
            commonWebpackConfig {
                outputFileName = "kotlin-new-features.js"
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.storyboard)
                implementation(libs.storyboard.easel)
                implementation(libs.storyboard.text)

                implementation(libs.haze)

                implementation(compose.material)
                implementation(compose.components.resources)
            }
        }

        jvmMain {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }

        all {
            languageSettings {
                enableLanguageFeature("ContextParameters")

                optIn("androidx.compose.animation.core.ExperimentalTransitionApi")
                optIn("androidx.compose.animation.ExperimentalAnimationApi")
                optIn("androidx.compose.animation.ExperimentalSharedTransitionApi")
                optIn("androidx.compose.ui.ExperimentalComposeUiApi")
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }
    }
}
