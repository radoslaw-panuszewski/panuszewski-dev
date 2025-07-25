@file:Suppress("OPT_IN_USAGE")

plugins {
    `compose-convention`
}

kotlin {
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
                implementation(projects.template)
            }
        }
    }
}
