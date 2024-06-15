---
title: "Spring I/O 2024 - subjective highlights"
date: 2024-06-15
toc: false
coverImage: spring-io-2024.png
tags:
  - spring-io
  - spring-boot
  - conference
---

No time to watch the entire conference? Here you can find a list of topics which I personally find the most interesting ;)
<!--more-->

## Autowiring improvements

Spring Core is like the heart for the entire Spring Framework where the dependency injection support resides. Guys responsible for this module has recently begun to revisit the old stuff and resolving some long-standing issues.

### Fast path for autowiring

The current autowire algorithm works as follows:

- find beans that match by type
- if multiple beans match, filter by qualifier
- if multiple beans still match, filter by the `@Primary` annotation
- if multiple beans still match, try to match the autowired parameter name with the bean name

What will change in Spring 6.2? The new version introduces a "fast path" for this algorithm. Instead of scanning for all matching beans and then applying the above conditions, the new approach will first try to find a bean that directly matches by name and then check it against the other three conditions. This optimization will likely be unnoticeable for small applications, but for those loading thousands of beans into the context, it can result in significant performance improvements.

### Fallback beans

In the previous section we mentioned the `@Primary` annotation. Let's quickly remind how it works:
```kotlin
@Configuration
class SomeConfiguration {
    
    @Bean
    fun defaultMessageProvider(): Provider<String> = { "default" }
    
    @Bean
    @Primary
    fun primaryMessageProvider(): Provider<String> = { "primary" }
    
    @Bean
    fun messagePrinter(messageProvider: Provider<String>) {
        println(messageProvider.get())
    }
}
// prints: "primary"
```
Despite providing 2 beans matching the `Provider<String>` type, only the one annotated with `@Primary` was autowired.

Now, Spring 6.2 will introduce the `@Fallback` annotation ([#26241](https://github.com/spring-projects/spring-framework/issues/26241)). Fallback beans work exactly in the opposite way compared to primary beans:
```kotlin
@Configuration
class SomeConfiguration {

    @Bean
    fun defaultMessageProvider(): Provider<String> = { "default" }
    
    @Bean
    @Fallback
    fun fallbackMessageProvider(): Provider<String> = { "fallback" }
    
    @Bean
    fun messagePrinter(messageProvider: Provider<String>) {
        println(messageProvider.get())
    }
}
// prints: "default"
```

Why is this useful? Imagine we're building a library that needs Jackson's `ObjectMapper` bean for internal use. If we simply declare it as a `@Bean`, it will become part of the context for every application using our library. Those apps most probably have their own `ObjectMapper` bean and we will break their autowiring by introducing a second one.

In Spring 6.2, we can mark our `ObjectMapper` bean as `@Fallback`. Within the context of our library, it will be the only bean of that type and will get autowired. In the context of a client application, the user's non-fallback bean will be chosen instead.

### Beans which cannot be autowired unless explicitly qualified

We can go even further with our `ObjectMapper` example. The `@Fallback` annotation will prevent it from breaking user's autowiring, but it won't prevent it from being accidentally autowired by the user. One way to fix that is by completely disabling autowiring for this particular bean:
```kotlin
@Bean(autowireCandidate = false)
fun objectMapper() = ObjectMapper()
```

The `autowireCandidate` is available since Spring 5.1 but can be inconvenient for us. By setting it to `false` we agree that the only way to access this bean is via:
```kotlin
applicationContext.getBeansOfType<ObjectMapper>()
```

Spring 6.2 will come with a similar flag named `defaultCandidate` ([#26528](https://github.com/spring-projects/spring-framework/issues/26528)):
```kotlin
@Bean(defaultCandidate = false)
fun objectMapper() = ObjectMapper()
```

It works similarly to `autowireCandidate` but it does not disable autowiring completely - it only requires explicit qualifier. In other words, bean with `defaultCandidate = false` will not leak to the client context by the type only. It's still injectable there, but the user must **really want it** and explicitly request the right qualifier.

## Spring HTTP Interface

Have you ever heard of Feign? Probably yes. It's a widely used library for declarative HTTP clients in Java and many people like it. In my case, it's rather a love-hate relationship, but let's come back to it later ;)

Since 6.0, Spring has its own Feign-like solution called [HTTP Interface](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-http-interface). The basic usage looks like that:
```kotlin
@HttpExchange("https://api.github.com")
interface GithubClient {

    @GetExchange("/repos/{owner}/{repo}/pull/{number}")
    fun getPullRequest(owner: String, repo: String, number: Long)
}
```

Looks similar to `@RequestMapping` and `@GetMapping` on controller class, isn't it? Well, it is by design.

In Feign, for example, you can use those `@RequestMapping` annotations directly in your client interface:
```kotlin
@FeignClient
@RequestMapping("https://api.github.com")
interface GithubClient {

    @GetMapping("/repos/{owner}/{repo}/pull/{number}")
    fun getPullRequest(owner: String, repo: String, number: Long)
}
```

However, Spring's HTTP Interface does not allow this. Why not? Initially, it might seem convenient to define your API in the controller and then copy-paste the same annotations to the client side. But as you proceed with this approach, more issues arise. Essentially, the `@RequestMapping` annotation offers a lot of flexibility for defining your controller. Single controller method can handle different types of requests, which is not what we want at the client side. We don't have any guarantee that copy-pasting annotations from controller to the client will actually work (and strange errors may appear). Therefore, the Spring team decided that `@HttpExchange` will expose only a subset of the `@RequestMapping` API.

Anyway, I find it awesome that Spring implemented this feature natively. Bye bye, Feign!

### Spring HTTP Client Autoconfiguration

Why I dislike Feign so much? A typical Feign client will look like this:
```kotlin
@FeignClient(configuration = GithubClient.Config::class)
interface GithubClient {

    // annotated methods...
    
    class Config {
        @Bean
        fun circuitBreaker(): CircuitBreaker {
            // ...
        }
    }
}
```

This little nested `Config` class is what confuses people a lot. You really want to omit the `@Configuration` annotation or exclude it from component scan in any other way. Otherwise, multiple Feign clients will register multiple conflicting `circuitBreaker` beans. The way it works is that Feign creates a "child Spring context" for every client, and this child context has its own set of beans. It allows every client to define its own configuration for circuit breaker... but it's also very easy to get it wrong.

Spring HTTP Interface currently doesn't have any autoconfiguration in place - you have to use a factory to create proxied interface instances. The ongoing work on that feature can be tracked in [#31337](https://github.com/spring-projects/spring-boot/issues/31337). Most likely, no child contexts will be used for that ;)

## Spring Boot Gradle Plugin + Buildpacks = ❤️

Always wanted to try out the hot new technologies like [GraalVM Native Image](https://www.graalvm.org/22.0/reference-manual/native-image/), [Spring AOT](https://docs.spring.io/spring-framework/reference/core/aot.html#aot.bestpractices.bean-registration) or [JVM Class Data Sharing (CDS)](https://docs.oracle.com/en/java/javase/17/vm/class-data-sharing.html#GUID-0260F857-A70E-4399-A1DF-A5766BE33285) but the configuration was just too painful? Buildpacks can do all those things for you if you only pass them a single environment variable. Even better, Spring Boot Gradle Plugin has really great integration with Buildpacks and you don't even need to install the `pack` CI to use it. Additionally, this way of using Buildpacks is much faster than traditional `pack` command, because it's all baked into your build and makes a good use of Gradle cache. 

It's as simple as applying a plugin:
```kotlin
plugins {
    id("org.springframework.boot") version "3.3.0"
}
```
and executing a task:
```bash
./gradlew bootBuildImage
```

Under the hood, it will run [paketobuildpacks/builder-jammy-base](https://github.com/paketo-buildpacks/builder-jammy-base) and go through all the buildpacks it includes. It will set up a JVM in the container, copy JAR file built by the `bootRun` task and specify it as an entrypoint. But it does a lot more! The image it produces is well-optimized and based on sensible defaults. It will even automatically [calculate](https://paketo.io/docs/reference/java-reference/#memory-calculator) how much memory your app needs and apply the `-Xmx` JVM option accordingly!

Below you can find quick configs for the cool stuff I mentioned earlier. Just copy-paste them into your `build.gradle.kts`, run `./gradlew bootBuildImage` and observe how the startup times of your app improved ;)

### Native image
```kotlin
plugins {
    id("org.springframework.boot") version "3.3.0"
    id("org.graalvm.buildtools.native") version "0.10.2"
}
```

### JVM with Spring AOT (Ahead of Time)
```kotlin
plugins {
    id("org.springframework.boot") version "3.3.0"
    id("org.springframework.boot.aot") version "3.3.0"
}

tasks {
    bootBuildImage {
        environment.put("BP_SPRING_AOT_ENABLED", "true")
    }
}
```

### JVM with CDS (Class Data Sharing)
```kotlin
plugins {
    id("org.springframework.boot") version "3.3.0"
}

tasks {
    bootBuildImage {
        environment.put("BP_JVM_CDS_ENABLED", "true")
    }
}
```

## Layered native images in GraalVM

Do you [hear elevator music](https://github.com/oracle/graal/issues/5327) in your head while waiting for the native build to finish? Read on if the answer is yes ;)

Let me introduce [Layered Native Images](https://github.com/oracle/graal/issues/7626). In a nutshell: it will allow you to use a pre-compiled Spring Framework (or any other framework you like) as base image for your application. Given that GraalVM won't have to recompile the good chunk of Spring code every time, it can result in significantly faster build times. Imagine that you can compile your app to native image almost as fast as building a fat jar. That could be a game-changer for many developers!

## Summary
It was my first time on Spring I/O and it was awesome 🚀 Thanks for the organizers for their amazing work!