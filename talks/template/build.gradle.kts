plugins {
    `compose-convention`
    `maven-publish`
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.storyboard)
                api(libs.storyboard.easel)
                api(libs.storyboard.text)
                api(libs.haze)
            }
        }
    }
}

publishing {
    publications.withType<MavenPublication> {
        groupId = "dev.panuszewski"
        version = "local"
    }
}
