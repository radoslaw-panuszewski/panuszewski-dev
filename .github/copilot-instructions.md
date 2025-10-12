# copilot-instructions.md

This file provides guidance to GitHub Copilot when working with code in this repository.

**Important:** Always ask clarifying questions if you don't understand something!

**Important:** The user will refer to `copilot-instructions.md` and `CLAUDE.md` as "instruction files" and will sometimes ask you to update them with lessons learned.

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

### AnimatedVisibility and Hidden File Animations: The Complete Solution
- **PROBLEM**: Hidden files in the IDE file tree (like `.gradle/libs.versions.toml`) need to animate both when appearing (forward navigation) and disappearing (backward navigation). AnimatedVisibility requires seeing the transition from `visible=false` to `visible=true` (and vice versa) while the item is still in the composition tree.
- **ROOT CAUSE #1 - No Backward Animation**: When navigating backward, the file was removed from the `allFiles` list (controlled by IdeLayout) before AnimatedVisibility could play the exit animation. The file needs to stay in the tree for the duration of the animation (300ms).
- **ROOT CAUSE #2 - No Forward Animation on First Appearance**: When the file first appeared, `visibilityTransition.targetState` was immediately true, so AnimatedVisibility never saw the `false→true` transition needed to trigger the enter animation.
- **ROOT CAUSE #3 - Visibility State Not Observable**: The `visiblePaths` Set was passed as a plain value to FileTreeItem, so when it changed, the composable didn't recompose to update the AnimatedVisibility's `visible` parameter.

#### The Complete Solution (Multi-Part Fix)

**Part 1: Add visibilityTransition to ProjectFile** (IDE.kt)
```kotlin
data class ProjectFile(
    // ... existing fields ...
    val visibilityTransition: Transition<Boolean>? = null,  // ✅ Added
)
```
- Allows IdeLayout to pass visibility state directly to the IDE component via the file object

**Part 2: Create Delayed Visibility Transition** (IdeLayout.kt)
```kotlin
val keepVisible = remember { mutableStateOf(false) }
val hasAppeared = remember { mutableStateOf(false) }

LaunchedEffect(visibilityTransition.targetState, visibilityTransition.currentState) {
    if (visibilityTransition.targetState && !hasAppeared.value) {
        // First appearance: delay 50ms for enter animation
        hasAppeared.value = false
        delay(50)
        hasAppeared.value = true
    } else if (!visibilityTransition.targetState) {
        if (visibilityTransition.currentState) {
            // Transition started: keep file in tree
            keepVisible.value = true
        } else {
            // Transition complete: wait 350ms for exit animation
            delay(350)
            keepVisible.value = false
        }
    }
}

// Keep file in allFiles during animations
val shouldInclude = visibilityTransition.targetState || 
                   visibilityTransition.currentState || 
                   keepVisible.value

// Create delayed visibility for AnimatedVisibility
val delayedVisibilityTransition = createChildTransition { 
    visibilityTransition.targetState && hasAppeared.value
}
```

**Part 3: Pass State Object Instead of Value** (IDE.kt)
```kotlin
// ❌ WRONG: Passing snapshot value
FileTreeItem(visiblePaths = visiblePaths.value)

// ✅ CORRECT: Passing State object
FileTreeItem(visiblePathsState = visiblePaths)

// In FileTreeItem signature
private fun FileTreeItem(
    visiblePathsState: State<Set<String>>  // State, not Set
)

// Reading the value inside composable scope
val isVisible = childNode.path in visiblePathsState.value
```

**Part 4: Use Transition-Based Visibility for Hidden Files** (IDE.kt)
```kotlin
val useVisibilityTransition = childNode.file?.visibilityTransition != null
val isVisible = if (useVisibilityTransition) {
    // Use transition targetState directly for hidden files
    childNode.file!!.visibilityTransition!!.targetState
} else {
    // Use visiblePaths mechanism for other files
    childNode.path in visiblePathsState.value
}

AnimatedVisibility(
    visible = isVisible,
    enter = expandVertically(tween(300)) + fadeIn(tween(300)),
    exit = shrinkVertically(tween(300)) + fadeOut(tween(300))
) {
    // Child content
}
```

#### Key Insights and Timing
- **50ms delay on first appearance**: Allows AnimatedVisibility to see `false→true` transition
- **Keep file in tree while `currentState == true`**: Compose transition system manages this duration
- **Additional 350ms delay after transition complete**: Ensures AnimatedVisibility finishes exit animation (300ms) before file removal
- **Transition targetState changes immediately**: This drives the AnimatedVisibility, triggering animations in both directions
- **File stays in tree longer than needed**: Better to delay removal than interrupt animation

#### Why This Works
1. **Forward (first time)**: File added with `hasAppeared=false` → 50ms delay → `hasAppeared=true` → AnimatedVisibility sees `false→true` → expand + fade in
2. **Backward**: `targetState=false` → AnimatedVisibility sees `true→false` → shrink + fade out → file kept in tree by `keepVisible` → removed after animation completes  
3. **Forward (subsequent)**: `hasAppeared` already true → AnimatedVisibility immediately sees `false→true` transition from previous removal → expand + fade in

#### Common Mistakes to Avoid
- ❌ Don't pass `visiblePaths.value` (snapshot) - pass the `State` object
- ❌ Don't remove files from tree immediately when target becomes false
- ❌ Don't rely on a single mechanism (`visiblePaths`) for both regular and hidden files
- ❌ Don't skip the initial delay on first appearance
- ❌ Don't forget to add both `enter` AND `exit` animations to AnimatedVisibility

#### Debugging Technique
When AnimatedVisibility animations don't work:
1. Add `println()` statements to track: `targetState`, `currentState`, `isVisible`, and whether item is in the composition tree
2. Verify the timing: Item must be IN TREE when `visible` changes
3. Check State observation: Ensure State objects are passed, not snapshots
4. Verify animation duration matches delay values (exit animation 300ms + buffer = 350ms delay)

### Animating Folders with Hidden Children
- **PROBLEM**: When a folder should appear/disappear together with its hidden children, wrapping both the folder and children separately in AnimatedVisibility causes them to appear at the same position, overlapping each other. The folder row and child items need to maintain their parent-child positioning while animating together.
- **ROOT CAUSE**: Separate AnimatedVisibility wrappers at different tree levels break the layout hierarchy. The folder gets wrapped at the root level, and children get wrapped inside the folder, causing independent animations that lose the spatial relationship.
- **WRONG APPROACH**: 
  ```kotlin
  // ❌ At root level
  AnimatedVisibility(folder visible) {
      FileTreeItem(folder)  // Folder row only
  }
  
  // ❌ Inside folder's children rendering
  AnimatedVisibility(child visible) {
      FileTreeItem(child)  // Child item
  }
  ```
  This causes folder and child to appear at same position, overlapping.

#### The Correct Solution: Wrap Folder + Children Together

**Implementation Strategy**:
1. **Detect folders with visibility transitions**: Check if folder has `visibilityTransition` property
2. **Wrap entire folder content**: Use Column to group folder row + all children
3. **Extract content rendering**: Separate the rendering logic to avoid code duplication

**Code Structure** (IDE.kt):
```kotlin
@Composable
private fun FileTreeItem(...) {
    val hasVisibilityTransition = node.file?.visibilityTransition != null && node.isFolder
    
    if (hasVisibilityTransition) {
        val isVisible = node.file!!.visibilityTransition!!.targetState
        AnimatedVisibility(
            visible = isVisible,
            enter = expandVertically(tween(300)) + fadeIn(tween(300)),
            exit = shrinkVertically(tween(300)) + fadeOut(tween(300))
        ) {
            FileTreeItemContent(...)  // Renders folder row + children together
        }
    } else {
        FileTreeItemContent(...)  // Normal rendering
    }
}

@Composable
private fun FileTreeItemContent(...) {
    Column {  // ✅ Groups folder row and children together
        Row { /* Folder row */ }
        
        // Render children
        if (node.isFolder && isExpanded) {
            node.children.forEach { child ->
                // Child rendering with own AnimatedVisibility if needed
            }
        }
    }
}
```

**Directory Visibility Mapping** (IdeLayout.kt):
```kotlin
val directoryVisibilityMap = remember(fileVisibilityMap, files) {
    val dirMap = mutableMapOf<String, Int>()
    for ((filePath, _) in files) {
        if (filePath.contains('/')) {
            val dirPath = filePath.substringBeforeLast('/')
            if (filePath in fileVisibilityMap.keys) {
                val childAppearAt = fileVisibilityMap[filePath]!!
                // Use earliest appearance time among all hidden children
                if (dirPath !in dirMap || childAppearAt < dirMap[dirPath]!!) {
                    dirMap[dirPath] = childAppearAt
                }
            }
        }
    }
    dirMap
}
```

**Apply Visibility Transition to Directories**:
```kotlin
value === DIRECTORY -> {
    val appearAtState = directoryVisibilityMap[filePath]
    
    if (appearAtState != null) {
        // Same visibility transition logic as hidden files
        val visibilityTransition = createChildTransition { globalState ->
            globalState >= appearAtState
        }
        
        // Same keepVisible and hasAppeared logic...
        
        ProjectFile(
            name = filePath.substringAfterLast('/'),
            path = filePath,
            isDirectory = true,
            visibilityTransition = delayedVisibilityTransition  // ✅ Added
        )
    } else {
        // Regular directory without visibility transition
        ProjectFile(name, path, isDirectory = true)
    }
}
```

#### Key Insights
- **Folder visibility based on children**: Directory appears when its earliest hidden child becomes visible
- **Single animation unit**: Folder row + children wrapped together in one AnimatedVisibility
- **Maintain tree structure**: Column layout preserves parent-child spatial relationship
- **Exclude folders from root-level wrapping**: Only wrap folders at FileTreeItem level, not in LazyColumn items

#### Common Mistakes
- ❌ Wrapping folder and children in separate AnimatedVisibility blocks
- ❌ Not using Column to group folder row and children together
- ❌ Wrapping folders at both root level AND FileTreeItem level
- ❌ Forgetting to exclude folders from root-level AnimatedVisibility wrapping (`&& !node.isFolder`)

#### When to Use This Pattern
- Folders with hidden children that should appear/disappear together
- File tree structures where parent visibility depends on child visibility
- Any hierarchical UI where parent and children should animate as a unit while maintaining their spatial relationship

### Multi-File State Synchronization: Clearing Markers on File Switch
- **PROBLEM**: When switching from one file back to another (e.g., from `wtf-app.gradle.kts` back to `build.gradle.kts`), the target file would immediately execute its next frame instead of waiting for the next state advance. This happened because marker frames (like `SWITCH_TO`, `EMOJI_SHOW`, etc.) accumulated and weren't being cleared properly.
- **ROOT CAUSE**: The `IdeLayout` composable tracks multiple types of marker frames (file switches, emoji display, pane operations) by scanning through all files' frames. When switching back to a file, these markers from previous states remained in the frame lists, causing the system to think it was still in a "marker frame state" and skip to the next regular frame.
- **SYMPTOMS**: 
  - After `.switchTo("build.gradle.kts")` from another file, the `build.gradle.kts` would immediately execute its next `.then { ... }` frame
  - The switch operation itself didn't consume a state, and the subsequent frame executed in the same state
  - File highlighting in the file tree would appear at wrong times
- **THE FIX**: Clear ALL marker-type frames when building the IDE state, not just the current one. This ensures that past markers don't interfere with current state logic:
  ```kotlin
  private fun buildIdeStateWithMapping(...) {
      val markerFrameIndices = files.flatMap { (path, frames) ->
          frames.indices.filter { i -> 
              val frame = frames[i]
              frame.code.isEmpty() && (
                  frame.tags.any { it.id.startsWith("SWITCH_TO:") } ||
                  frame.tags.any { it.id == "EMOJI_SHOW" } ||
                  frame.tags.any { it.id == "EMOJI_HIDE" } ||
                  frame.tags.any { it.id.startsWith("OPEN_LEFT_PANE:") } ||
                  frame.tags.any { it.id.startsWith("OPEN_RIGHT_PANE:") } ||
                  frame.tags.any { it.id == "CLOSE_LEFT_PANE" } ||
                  frame.tags.any { it.id == "CLOSE_RIGHT_PANE" } ||
                  frame.tags.any { it.id == "HIDE_FILE_TREE" } ||
                  frame.tags.any { it.id == "SHOW_FILE_TREE" }
              )
          }.map { path to it }
      }
      
      // Clear all marker frames from files
      val cleanedFiles = files.mapValues { (path, frames) ->
          frames.filterIndexed { index, _ ->
              (path to index) !in markerFrameIndices
          }
      }
  }
  ```
- **KEY INSIGHT**: Marker frames are "consumed" across ALL files simultaneously, not just in the active file. When you execute a switch operation in file A, that marker must be cleared from file A before processing file B's frames, otherwise the stale marker will interfere with file B's state logic.
- **DEBUGGING TECHNIQUE**: 
  - Add `println()` to track when frames are executed: "Executing frame X in file Y at state Z"
  - Print the frame content and tags to identify marker frames
  - Check if frames execute in unexpected states (e.g., two frames executing when only one state advance occurred)
  - Verify that marker frames are properly removed from frame lists after being processed
- **BEST PRACTICE**: When implementing new marker-based operations (emoji display, pane operations, etc.), always ensure they are added to the marker detection logic in `buildIdeStateWithMapping()` so they get properly cleared after execution

### Simultaneous Animations During Scene Transitions: Non-Blocking vs Blocking Animations
- **PROBLEM**: When implementing animations that should continue during scene transitions (e.g., scrolling an image while cross-fading to the next scene), using `transition.animateInt()` causes the scene to wait for the animation to complete before starting the exit transition. The scrolling and cross-fade happen sequentially instead of simultaneously.
- **ROOT CAUSE**: `transition.animateInt()` and similar transition-based animations are **blocking** - the storyboard framework waits for them to complete before moving to the next scene. This is by design, as the framework ensures all state animations finish before transitioning.
- **SYMPTOMS**:
  - Animation completes fully, then the scene transition (fade out) begins
  - Both animations have the same duration but happen one after another
  - Total time is sum of both durations instead of the maximum of the two
- **THE SOLUTION**: Use Compose's `animateFloatAsState` or `animateIntAsState` instead of `transition.animateInt()`. These are **non-blocking** animations that run independently of the scene transition lifecycle:
  ```kotlin
  // ❌ WRONG: Blocking animation
  val scrollPosition by transition.animateInt(
      transitionSpec = { tween(durationMillis = 3000, easing = LinearEasing) },
      targetValueByState = { if (it == Frame.End) 10886 else 0 }
  )
  
  // ✅ CORRECT: Non-blocking animation
  var shouldScroll by remember { mutableStateOf(false) }
  
  LaunchedEffect(transition.currentState) {
      if (transition.currentState == Frame.End) {
          shouldScroll = true
      }
  }
  
  val scrollProgress by animateFloatAsState(
      targetValue = if (shouldScroll) 1f else 0f,
      animationSpec = tween(durationMillis = 3000, easing = LinearEasing)
  )
  
  val scrollPosition = (scrollProgress * 10886).toInt()
  ```
- **HOW IT WORKS**:
  1. `LaunchedEffect` detects when `transition.currentState` becomes `Frame.End`
  2. Sets `shouldScroll = true`, triggering the animation
  3. `animateFloatAsState` runs independently - doesn't block the scene transition
  4. Scene's exit transition (fadeOut) starts immediately
  5. Both animations run simultaneously for their respective durations
- **KEY INSIGHT**: Compose state animations (`animateFloatAsState`, `animateIntAsState`) are independent of the storyboard transition system. Use them when you need animations to overlap with scene transitions.
- **WHEN TO USE**:
  - ✅ Use `animateFloatAsState`/`animateIntAsState` when animations should continue during scene transitions
  - ✅ Use `transition.animateInt()`/`transition.animateFloat()` when animations should complete before transitioning
- **DEBUGGING TECHNIQUE**:
  - If animations wait to complete before transitioning, check if you're using `transition.animate*()` methods
  - Replace with Compose's `animate*AsState()` and trigger via state changes detected by `LaunchedEffect`
- **BEST PRACTICE**: For visual effects that enhance transitions (scrolling, rotating, zooming), use non-blocking Compose animations. For content changes that must complete before transitioning (showing/hiding critical elements), use blocking transition animations.