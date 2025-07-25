plugins {
    `compose-convention`
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.storyboard)
                api(libs.storyboard.easel)
                api(libs.storyboard.text)
            }
        }
    }
}
