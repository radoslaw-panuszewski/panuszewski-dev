@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}

rootProject.name = "talks"

include("kotlin-new-features")
include("template")
include("future-of-jvm-build-tools")
include("purging-chaos-from-your-gradle-build")
includeBuild("../storyboard")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
