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

tasks {
    jvmDistZip {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    jvmDistTar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    installJvmDist {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}