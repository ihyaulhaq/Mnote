# Mnote

Single-module Android app (Jetpack Compose) — financial recording application with a simple design with neo-brutalist UI, records expenses and provides statistical summaries regarding these expenses.

## Critical Rules

- **Design System**: All UI uses neo-brutalist style. Components get their look from `NSurface` (thick borders, hard offset shadows). Use `NColors` for palette, never raw `Color(...)`.
- **AGP 9.x**: Uses `compileSdk { version = release(37) { minorApiLevel = 1 } }` syntax, not the older `compileSdk = 37` integer form.
- **Configuration cache** is enabled in `gradle.properties`. Avoid tasks that break it (e.g., shared build services without proper `@CacheableTask` annotation).
- **No navigation library yet** — Navigation Compose dependency is commented out in `app/build.gradle.kts:47`. Currently single-screen.
- **Java 11** source/target compatibility.

## Build & Run

```bash
./gradlew assembleDebug        # build debug APK
./gradlew installDebug         # build + install on connected device/emulator
./gradlew test                 # unit tests
./gradlew connectedAndroidTest # instrumented tests (requires running emulator)
```

No linter, formatter, or typecheck beyond Kotlin compiler defaults.

## Project Structure

Single `app` module. Package: `com.github.ihyaulhaq.mnote`

- `ui/components/` — Reusable neo-brutalist composables: `NSurface`, `NButton`, `NTextField`
- `ui/home/` — `HomeScreen` (input expense)
- `ui/theme/` — Two theme systems:
  - `Theme.kt` / `Color.kt` / `Type.kt` — **Active design system**

## Architecture Patterns

- **Single Activity**: Use `MainActivity` as the only Activity with Jetpack Compose
- **Unidirectional Data Flow**: State flows down, events flow up
- **State Hoisting**: Keep state in the parent, pass down as parameters
- **Side Effects**: Use `LaunchedEffect`, `rememberCoroutineScope` for async operations
- **ViewModel**: For complex state management (not yet implemented in this project)

## Jetpack Compose Patterns

### State Management
- Use `remember` for UI state that doesn't need to survive configuration changes
- Use `rememberSaveable` for state that needs to survive configuration changes
- Use `mutableStateOf` for observable state
- Avoid `derivedStateOf` unless you have a specific performance need

### Side Effects
- Use `LaunchedEffect` for one-time operations (navigation, analytics)
- Use `rememberCoroutineScope` for event-triggered coroutines
- Use `DisposableEffect` for cleanup (listeners, subscriptions)
- Avoid side effects in `Composition`

### Performance
- Use `key` in `LazyColumn`/`LazyRow` for stable recomposition
- Use `Modifier Modifier Modifier` (extension functions) for reusable modifiers
- Avoid creating new objects in `@Composable` functions
- Use `@Stable` and `@Immutable` annotations for performance optimization

### Navigation
- Currently single-screen (no navigation)
- When adding Navigation Compose, use type-safe arguments
- Keep navigation logic in a dedicated file

## Testing

### Unit Tests
```bash
./gradlew test
```
- Test ViewModel logic, data models, utility functions
- Use JUnit 4 with Mockito/Mockk for mocking

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```
- Test UI interactions with Compose Testing APIs
- Use `ComposeTestRule` for UI testing
- Test navigation, user flows, accessibility

### Testing Strategies
- **Given-When-Then** pattern for test structure
- **Arrange-Act-Assert** for test organization
- Use `@Test` annotation for test methods
- Use `@Before`/`@After` for setup/teardown

## Performance Tips

- **Recomposition**: Minimize by using stable types and `key` in lists
- **Memory**: Avoid leaking Activity/Context in long-lived objects
- **Battery**: Use `WorkManager` for background tasks, not `AlarmManager`
- **Rendering**: Use `Modifier Modifier Modifier` (extension functions) for reusable modifiers
- **Startup**: Minimize Application class work, use `App Startup` library

## Gotchas

- `NSurface` padding semantics: it adds `padding(end=shadowOffset, bottom=shadowOffset)` to its parent Box to make room for the shadow. Passing a size modifier to `NButton` or `NTextField` affects the front face, not the shadow area.
- Neo-brutalist colors use `NColors`, not Material `Color`. The `NColors.Black` is `#111111`, not pure black.

## AI Rules

### Context7

Always use Context7 MCP whenever the task involves:

- library or framework documentation
- API references
- dependency setup
- installation steps
- configuration
- migrations
- code generation using external libraries

Only fall back to internal knowledge if Context7 does not contain the requested library.