plugins {
    `storyboard-convention`
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.template)
                implementation("dev.panuszewski:template-jvm:local")
            }
        }
    }
}
