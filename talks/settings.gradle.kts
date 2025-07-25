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
includeBuild("../storyboard")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
