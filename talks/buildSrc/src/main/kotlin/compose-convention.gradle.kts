@file:Suppress("OPT_IN_USAGE")

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jetbrains.compose.hot.reload)
}

kotlin {
    jvmToolchain(24)

    jvm()
//    wasmJs {
//        browser()
//    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.material)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
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
