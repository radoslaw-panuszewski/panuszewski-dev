# copilot-instructions.md

This file provides guidance to GitHub Copilot when working with code in this repository.

**Important:** Always ask clarifying questions if you don't understand something!

**Important:** Remember to always update `copilot-instructions.md` and `CLAUDE.md` after performing a task if you learned something new!

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

### Adding Simple Language Support
For basic syntax highlighting using regex patterns (like strings and numbers):
1. **Create the highlighter file**: Follow the pattern in `Hocon.kt` and `Toml.kt` by creating a new file like `Groovy.kt`
2. **Define regex patterns**: Use simple regex for basic elements (strings, numbers, keywords)
3. **Register the language**: Add the new language to the `Language` enum and the `String.highlight()` function in `Language.kt`
4. **Pattern examples**:
    - Single quotes: `"""('.*?')""".toRegex()`
    - Double quotes: `"""(".*?")""".toRegex()`
    - Numbers: `"""\b(\d+(?:\.\d+)?)\b""".toRegex()`

### Critical Regex Highlighting Pitfalls
- **MISTAKE: Overlapping Pattern Matches**: When multiple regex patterns match the same text (e.g., numbers inside quoted strings), later patterns can override earlier ones, causing incorrect highlighting
- **ROOT CAUSE**: The `forEachOccurrence` function applies all matches sequentially. If string patterns match `'2.2.20'` and number patterns match `2.2` and `20` within the same text, the number highlighting overrides the string highlighting
- **CORRECT APPROACH**: Track ranges that have already been styled and prevent overlapping patterns from overriding previous styling:
  ```kotlin
  val stringRanges = mutableSetOf<IntRange>()

  // Apply string highlighting first and track ranges
  SINGLE_QUOTE_STRING_REGEX.forEachOccurrence(text) { range ->
      addStyle(codeStyle.string, range)
      stringRanges.add(range)
  }

  // Only apply number highlighting if not inside a string
  NUMBER_REGEX.findAll(text).forEach { match ->
      val range = match.groups[1]?.rangeCompat
      if (range != null && !isInsideAnyRange(range, stringRanges)) {
          addStyle(codeStyle.number, range)
      }
  }
  ```
- **KEY LESSON**: Always consider pattern precedence when implementing syntax highlighting. Strings should generally take precedence over numbers, keywords over identifiers, etc.
- **TESTING REQUIREMENT**: Test with realistic code samples that contain overlapping patterns (like `'2.2.20'`) to ensure correct highlighting behavior

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

### The workflow
1. **Start by asking clarifying questions**: Be specific, concise and actionable
2. **Write the code**: Do all the changes needed to accomplish the task. Follow the guidelines fron the **Writing code** section
3. **ALWAYS Launch Presentation After Changes**: MANDATORY - After making ANY code changes to presentation components, scenes, or layouts, immediately run the presentation to verify the changes work correctly. This is critical for catching issues early.
4. **Launch Presentation**: Start the desktop presentation application to display the current presentation state. Inform the user that the presentation is now running and they should interact with it to test functionality, visual appearance, transitions, and overall user experience.
5. **User Testing Phase**: Allow the user to freely explore and test the presentation. Do not interrupt this process. Wait patiently for the user to close the application when they are finished testing.
6. **Feedback Collection**: Once the user closes the presentation app, ask them directly: 'How does the presentation look? Are you satisfied with the current state, or are there issues that need to be addressed?' Listen carefully to their response and probe for specific details about any problems they encountered.
7. **Update `copilot-instructions.md` and `CLAUDE.md` with lessons learned**: When issues are identified:
    - Document the specific problems mentioned by the user
    - Analyze how these issues affect the presentation's visual appearance, functionality, and user experience
    - Update the `copilot-instructions.md` and `CLAUDE.md` with:
        * Clear descriptions of the mistakes that were made
        * Detailed explanation of how these mistakes impacted the presentation
        * Specific instructions on how to avoid these issues in future iterations
        * Any relevant best practices or constraints that should be followed

## Writing code
- Do not put any comments in the code
- Do not use negative offsets unless absolutely necessary, prefer spacers instead
- Always check the reference implementation in future-of-jvm-build-tools
- Extract reusable composable functions if applicable
- If you extract a reusable composable function, move most of the logic there - the scene should only call the composable and provide data (like the title and agenda content)
- If the reusable part of the logic has a fixed number of states, extract it as reusable scene, not composable function. The reusable scene (which is an extension function with StoryboardBuilder receiver) should accept data as parameters.
- Extract common patterns to template subproject
- The future-of-jvm-build-tools talk is a baseline, treat is as a reference implementation. The appearance (like margins, text size, colors, etc.) and behavior (transitions between scenes) should work the same as in the reference implementation. However, you can refactor the code to improve its
  readability and extensibility.
- Use TitleScaffold in every scene, unless instructed otherwise
- Implement scene boundaries using titles displayed at the top, allowing multiple related titles to be grouped when contextually appropriate
- Ensure consistent navigation and flow patterns across all scenes
- Refactor existing components when necessary, always preserving their usability in existing talks
- Write clean, maintainable Compose code following established project patterns
- Ensure all animations and transitions are smooth and purposeful
- Implement proper state management for complex scene transitions
- Create modular, testable components that can be easily reused
- Prefer helpers like `SlideFromTopAnimatedVisibility`, `FadeOutAnimatedVisibility`, `FadeInOutAnimatedVisibility` over specifying the transitions manually to `AnimatedVisibility`. If you discover a new repeating pattern, extract a new similar helper
- Use `RevealSequentially` for bulletpoint lists
- If you implement animation when element B appears under element A with spacer between them, you don't need to wrap the spacer in `AnimatedVisibility`
- Never write lambda like this `{ fullHeight -> fullHeight }`, just use `{ it }` instead
- Prefer `it in listOf(0, 4)` over `it == 0 || it == 4`
- Always keep scenes in separate `*.kt` files
- Prefer padding over offset for positioning elements unless offset is absolutely necessary (e.g., for sliding animations). Using offset can cause elements to be positioned outside screen bounds
- When implementing sliding title scenes, use Box layout with absolute positioning for the title animation, and position other elements using padding to ensure they stay within screen bounds
- For sliding title animations, animate both text style (h2 to h4) and vertical offset simultaneously to achieve smooth transitions
- Do not add the `*Scene` suffix to scene function names
- When refactoring code duplication in scenes, create reusable `@Composable` functions with `SceneScope<Int>` receiver instead of trying to wrap entire scene logic. This allows the component to access `transition` and other scene context while remaining reusable
- Use specific generic types like `SceneScope<Int>` rather than generic `<R>` parameters when the concrete type is known across the codebase
- Extract only the UI logic into reusable composables, not the entire scene structure. Scenes should remain independent but can share common composable components

### Order of functions in a file

When extracting private methods, always follow the principle that call order of the private functions should match the order of their appearance in the code.

It should look like this:
```kotlin
fun doSomething() {
    firstOperation()
    secondOperation()
}

fun firstOperation() {
    connectToDatabase()
    fetchData()
}

fun connectToDatabase() {
    // ...
}

fun fetchData() {
    // ...
}

fun secondOperation() {
    sendEvent()
}

fun sendEvent() {
    // ...
}
```

## Lessons Learned

### Component Refactoring Best Practices
- **SlidingTitleScaffold Implementation**: When creating reusable scaffold components, follow the pattern of providing both String and AnnotatedString variants for flexibility, similar to existing components like TitleScaffold and AnimatedTitle
- **Box Layout for Sliding Animations**: Use Box layout with absolute positioning for sliding title animations, and position content using padding (not offset) to ensure elements stay within screen bounds. The 248.dp top padding works well for SlidingTitle positioning
- **Component Extraction**: When extracting reusable components from duplicated scene logic, create separate files for better organization and maintainability. Move complex layouts like SlidingTitleScaffold to their own files rather than keeping them in utility files
- **Scene Refactoring**: When updating scenes to use new scaffold components, remove manual Box layouts and positioning code. The scaffold should handle all layout concerns, leaving scenes to focus only on content and state management

### Critical Layout Mistakes and Fixes
- **MISTAKE: Wrong SlidingTitleScaffold Layout**: Initially implemented SlidingTitleScaffold with Column and fixed padding, which broke the sliding title animation positioning. Titles appeared in top-left corner and moved out of bounds instead of sliding properly
- **ROOT CAUSE**: Failed to study original implementations carefully. Original scenes used `Box(contentAlignment = Alignment.Center)` as the base layout, then positioned content with `offset()` or `padding()` relative to the center
- **CORRECT APPROACH**: SlidingTitleScaffold should use `Box(contentAlignment = Alignment.Center)` to maintain the same layout structure as original scenes. Content positioning (offsets/padding) should remain in the scene implementations, not in the scaffold
- **KEY LESSON**: When extracting reusable components, preserve the EXACT same layout structure and positioning logic as the original implementations. Do not change the fundamental layout approach (center-based vs absolute positioning) when creating scaffolds
- **TESTING REQUIREMENT**: Always run presentations immediately after layout changes to catch visual regressions. Compilation success does not guarantee correct visual behavior

### Critical Compose State Management Bug: CodeSample equals/hashCode
- **CRITICAL BUG DISCOVERED**: The `CodeSample.equals()` and `hashCode()` methods were missing the `warningTags` property, causing Compose to treat CodeSample instances with different warning tags as equal. This prevented UI recomposition when only warning tags changed.
- **SYMPTOMS**: Warning underlines created with `changeTagType(tag, WARNING)` would never appear in the UI during scene transitions, even though debug logs showed the warning tags were being added correctly
- **ROOT CAUSE**: When properties are missing from `equals()` and `hashCode()`, Compose's state management system cannot detect changes to those properties, preventing recomposition
- **FIX REQUIRED**: Added `warningTags` to both `equals()` and `hashCode()` methods:
  ```kotlin
  override fun equals(other: Any?): Boolean {
      // ... existing checks ...
      if (warningTags != other.warningTags) return false  // ✅ Added
      return true
  }

  override fun hashCode(): Int {
      // ... existing fields ...
      result = 31 * result + warningTags.hashCode()  // ✅ Added
      return result
  }
  ```
- **DEBUGGING TECHNIQUE**: Use debug output to compare CodeSample instance hash codes - if instances with different properties have the same hash code, check for missing properties in equals/hashCode
- **COMPOSE GOLDEN RULE**: ALL properties that affect UI rendering MUST be included in both `equals()` and `hashCode()` methods. Missing properties cause silent state management failures that are extremely difficult to debug
- **PREVENTION**: When adding new properties to data classes used in Compose state, immediately add them to equals/hashCode. Consider using Kotlin data classes which auto-generate these methods correctly