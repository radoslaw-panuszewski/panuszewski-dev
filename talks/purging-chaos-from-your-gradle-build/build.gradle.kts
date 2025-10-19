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

fun pluginMarker(provider: Provider<PluginDependency>) =
    with(provider.get()) {
        "$pluginId:$pluginId.gradle.plugin:$version"
    }