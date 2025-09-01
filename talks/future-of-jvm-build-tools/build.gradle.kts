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
    register("printMessage") {
        println("Configuring the task...")
        doLast {
            println("Executing the task...")
        }
    }
}
