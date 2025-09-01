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
    register("killGroovy") {
        outputs.file(layout.buildDirectory.file("out.txt"))
        outputs.cacheIf { true }
        doLast {
            println("Bang!")
            outputs.files.singleFile.writeText("Bang!")
        }
    }
}
