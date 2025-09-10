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

println("Hello from configuration phase!")

tasks {
    register("sayHello") {
        outputs.cacheIf { true }
        doLast {
            println("Hello from execution phase!")
            outputs.files.singleFile.writeText("Hello")
        }
    }
}
