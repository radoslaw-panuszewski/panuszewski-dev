# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Priorities

**Prefer modifying talks over library:** When implementing features or fixes, try to modify code in the `talks/` directory first. Only modify the `storyboard/` library if there's no other way to achieve the goal. The storyboard library is a local fork that may be contributed upstream, so changes should be well-considered.

## Repository Structure

This is a personal development repository containing two main projects:

### Storyboard Library (`storyboard/`)
A Compose Multiplatform library for building presentations, similar to reveal.js or Spectacle. The library uses scenes (slides) and states (advancements) instead of traditional slide terminology.

**Important:** This is a local fork of https://github.com/bnorm/storyboard and changes made here are potentially subject to be contributed upstream.

**Key directories:**
- `storyboard/storyboard/` - Core library with building blocks for storyboards
- `storyboard/storyboard-easel/` - UI components for desktop and web usage
- `storyboard/storyboard-text/` - Text animation and code rendering utilities
- `storyboard/examples/` - Example storyboards (basic, interactive, shared)

### Talks (`talks/`)
Presentation projects built using the Storyboard library. Each talk is a separate Kotlin Multiplatform project that can target JVM desktop and WASM web.

**Current talks:**
- `future-of-jvm-build-tools/`
- `keeping-your-build-clean/`
- `kotlin-new-features/`
- `template/` - Base template for new talks

## Build Commands

### Storyboard Library
Navigate to `storyboard/` directory:
- **Build all modules:** `./gradlew build`
- **Run tests:** `./gradlew test`
- **Generate documentation:** `./gradlew dokkaGeneratePublicationHtml`
- **Build site with examples:** `./gradlew site`
- **Publish to local repository:** `./gradlew publishToMavenLocal`

### Individual Talks
Navigate to `talks/` directory:
- **Build specific talk:** `./gradlew :<talk-name>:build`
- **Run JVM version:** `./gradlew :<talk-name>:runJvm`
- **Build WASM web version:** `./gradlew :<talk-name>:wasmJsBrowserDistribution`
- **Generate JVM distribution:** `./gradlew :<talk-name>:installJvmDist`
- **Build all talks:** `./gradlew build`

Example: `./gradlew :future-of-jvm-build-tools:runJvm`

**After implementing features:** Always run `:installJvmDist` to generate an up-to-date distribution. This creates a standalone presentation that can be run without Gradle or IntelliJ during actual talks.

### Running Presentations
- **JVM Desktop:** Use `:runJvm` task or execute the generated distribution from `build/install/<talk-name>/bin/`
- **Web (WASM):** Build with `wasmJsBrowserDistribution` and serve the generated files

## Code Syntax Highlighting

The storyboard-text library supports syntax highlighting for code examples in presentations. If you need to add support for a new programming language:

1. **Check existing support:** Look in `storyboard/storyboard-text/` for existing language support
2. **Simple cases:** Use regex-based highlighting for basic needs
3. **Complex cases:** If ANTLR grammar is available:
   - Download the grammar file and place it in `storyboard/storyboard-text/antlr/`
   - Use tasks like `:generateKotlinGrammarSource`, `:generateXmlGrammarSource` to generate lexer/parser
   - Implement the generated interfaces for proper syntax highlighting

Available grammar generation tasks in storyboard-text:
- `:generateKotlinGrammarSource`
- `:generateXmlGrammarSource`
- (Other language-specific tasks as needed)

## Architecture Notes

### Storyboard Concepts
- **Storyboard:** The entire presentation (equivalent to a slide deck)
- **Scene:** Individual slide with multiple states
- **State:** Different advancement stages within a scene
- **Frame:** Rendering unit that includes start/end frames for transitions

### Build Conventions
- Talks use custom Gradle convention plugins (`storyboard-convention`, `compose-convention`)
- Projects use Kotlin Multiplatform with JVM and WASM JS targets
- Compose Multiplatform with experimental features enabled (ContextParameters, animation APIs)
- Version catalogs managed through Gradle libs for dependency management

### Key Technologies
- Kotlin Multiplatform
- Compose Multiplatform
- Gradle with Kotlin DSL
- WASM target for web deployment
- Maven publishing for library distribution