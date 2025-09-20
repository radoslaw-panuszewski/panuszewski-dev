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
        mavenCentral()
        google()
    }
}

rootProject.name = "talks"

include("kotlin-new-features")
include("template")
include("future-of-jvm-build-tools")
include("keeping-your-build-clean")
includeBuild("../storyboard")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
