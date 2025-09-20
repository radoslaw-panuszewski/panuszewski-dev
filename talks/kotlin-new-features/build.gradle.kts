@file:Suppress("OPT_IN_USAGE")

plugins {
    `storyboard-convention`
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.template)
            }
        }
    }
}

