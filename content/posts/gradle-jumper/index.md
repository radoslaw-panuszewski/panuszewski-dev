---
title: "Gradle Jumper - plugin for better IDE navigation"
date: 2024-04-20
toc: false
coverImage: jumping.png
tags:
  - gradle
  - intellij
  - IDE plugin
---

Gradle has been recently introducing more and more typesafe accessors, especially for Kotlin DSL buildscripts. Wouldn't it be nice to jump to a subproject via its typesafe accessor?
<!--more-->

Let's say we have a project with the following structure:
```
.
├── build.gradle.kts
├── settings.gradle.kts
├── ...
└── my-subproject/
    └── build.gradle.kts
```

```kotlin {title="build.gradle.kts"}
plugins {
    java
}

dependencies {
    implementation(projects.mySubproject)
}
```

```kotlin {title="settings.gradle.kts"}
include(":my-subproject")
// still needed in Gradle 8.7
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
```

What happens if you Cmd-Click on the `mySubproject`? Well, the IntelliJ will bring you to the `RootProjectAccessor.java` file:
```java
public class RootProjectAccessor extends TypeSafeProjectDependencyFactory {
    /**
     * Creates a project dependency on the project at path 
     * ":my-subproject"
     */
    public MySubprojectProjectDependency getCamelCaseSubproject1() { 
        return new MySubprojectProjectDependency(
            getFactory(), 
            create(":my-subprojet")
        ); 
    }
```

Not really useful, isn't it? Let's install some plugin to fix it ;)

{{< h3-with-icon "pluginIcon.svg" "Gradle Jumper" >}}

-> [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/24207-gradle-jumper)

-> [GitHub](https://github.com/radoslaw-panuszewski/gradle-jumper)

{{< youtube _GOiKZQXzfI >}}

The Gradle Jumper plugin  adds enhanced "go to declaration" support for Gradle typesafe (and not typesafe) accessors. Instead of going to the generated code, you will jump directly to the place which is semantically referenced.

So basically, in our example, after Cmd-Clicking on the `mySubproject`, IDE will open the `my-subproject.gradle.kts` file for you:

```kotlin {title="my-subproject/build.gradle.kts"}
plugins {
    java
}

println("Hello from my-subproject!")
```

The plugin currently supports:
* subproject references (shown in our example)
* precompiled script plugin references

If it happens to make your life a little easier, don’t hesitate to throw a ★ on GitHub and rate the plugin on the JetBrains marketplace 😉