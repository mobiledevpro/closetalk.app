# AGENTS.md - AI Development Guide for Android Jetpack Compose MVI Template

## Project Overview

This is an Android chat app built with **Jetpack Compose**, **Kotlin 2.4**, and **MVI (
Model-View-Intent) architecture** with heavy modularization. The codebase separates concerns into 8
core modules and 8+ feature modules, each with clear responsibilities.

**Key Stack**: Compose, Kotlin Coroutines, Koin DI, Room DB, Firebase, Material 3, Navigation
Compose

---

## Architecture Patterns

### MVI (Model-View-Intent)

**Pattern**: State-driven UI with unidirectional data flow.

- **ViewModel**: Extends `BaseViewModel<State>` from `core:ui` module
- **State**: Sealed interface implementing `UIState` (e.g., `Loading`, `Success(data)`,
  `Fail(error)`)
- **Intent**: User actions trigger state changes via UseCase → ViewModel → StateFlow

**Example in `feature:people_list`**:

```kotlin
class PeopleListViewModel(val getPeopleListUseCase: GetPeopleListUseCase) : BaseViewModel<PeopleProfileUIState>() {
    override val initialState = PeopleProfileUIState.Loading
    override fun observeState() = getPeopleListUseCase.execute()
        .map { result -> try { PeopleProfileUIState.Success(result.getOrThrow()) } catch (t) { PeopleProfileUIState.Fail(t) } }
}

sealed interface PeopleProfileUIState : UIState {
    data object Loading : PeopleProfileUIState
    data class Success(val profileList: List<PeopleProfile>) : PeopleProfileUIState
    data class Fail(val throwable: Throwable) : PeopleProfileUIState
}
```

**Consume in Compose**:

```kotlin
@Composable
fun PeopleListScreen(vm: PeopleListViewModel = rememberNavViewModel { modules }) {
    val state by vm.uiState.collectAsState()
    when(state) { /* render based on state */ }
}
```

### Modular Architecture

**Two layers**:

- **`:core:*` modules**: Shared infrastructure (UI, DI, Navigation, Domain, Coroutines, Analytics,
  Database, Utils)
- **`:feature:*` modules**: Feature screens (people_list, home, chat_list, user_profile, etc.)

**Dependency flow**: `Feature modules → Core modules` (never the reverse). Navigation orchestrates
feature composition.

**Module names use underscores**: `people_list` → namespace `com.mobiledevpro.people.list` (via
`core-module.gradle.kts`)

---

## Build System & Conventions

### Gradle Plugins

Located in `build-logic/src/main/kotlin/`:

- **`core-module.gradle.kts`**: Applied to all core modules. Sets namespace, minSdk, SDK versions,
  desugaring, flavors (dev/production)
- **`feature-module.gradle.kts`**: Extends core-module, adds Compose, auto-includes core
  dependencies (ui, di, domain, coroutines, util, analytics, lifecycle, coil)
- **`core-compose-module.gradle.kts`**: For core modules needing Compose (e.g., `core:ui`)
- **`kotlin-convention.gradle.kts`**: Kotlin/JDK configuration

**Example**: A feature module only needs `plugins { id("feature-module") }` to get all standard
dependencies.

### Version Management

- **`gradle/libs.versions.toml`**: Single source for all versions (Kotlin 2.4, Compose BOM
  2026.05.01, AGP 9.2.1, Koin 4.2.1, Room 2.8.4, Firebase BOM 34.14.0)
- Release versions in `gradle/libs.versions.toml` (`app-version-code`, `app-version-name`)

### Build Variants

- **Flavors**: `dev` (default, app ID `.apptemplate.compose`) and `production` (`.closetalk.app`)
- **Build Types**: Debug (Crashlytics disabled) and Release (minified, Crashlytics enabled)
- **Build output**: `.aab` files auto-renamed via `RenameBundleTask` in `app/build.gradle.kts`

### Key Build Commands

```bash
./gradlew clean build --profile          # Build with performance profiling
./gradlew bundleProductionRelease        # Create AAB for Play Store
./gradlew createModuleGraph              # Update module graph in README
./gradlew --profile                      # Generate build metrics
```

---

## Dependency Injection with Koin

### Pattern

- **Module registration**: Each feature defines a `di/Module.kt` with
  `val featureNameModule = module { ... }`
- **Scope pattern**: Use `scope<ViewModel>` to tie dependencies to ViewModel lifecycle
- **ViewModels registered as**: `viewModelOf(::ClassName)` (uses SavedStateHandle automatically)
- **Other dependencies**: `scopedOf(::UseCase)` for classes tied to scope, `singleOf()` for
  singletons

**Example from `feature:people_list`**:

```kotlin
val featurePeopleListModule = module {
    scope<PeopleListViewModel> {
        viewModelOf(::PeopleListViewModel)
        scopedOf(::GetPeopleListUseCase)
    }
}
```

### Consuming in Compose

Via `core:di` helper: `rememberNavViewModel<ViewModelType> { listOf(module) }` - handles module
loading/unloading automatically.

---

## State & Coroutines

### StateFlow Pattern (core:ui BaseViewModel)

- `uiState: StateFlow<State>` exposed lazily
- Uses `SharingStarted.WhileSubscribed(stopTimeoutMillis=0, replayExpirationMillis=9000ms)`
- Handles lifecycle safety automatically

### Testing Coroutines

- Use **Turbine** (`app.cash.turbine`) for Flow testing: `vm.uiState.test { awaitItem() ... }`
- Use **Robolectric** for instrumented tests without device
- Use `StandardTestDispatcher` with test scheduler for deterministic timing
- See `PeopleListViewModelTest.kt` for pattern

---

## Testing Conventions

### Test Structure

- **Unit tests**: `src/test/kotlin` - mock data, no Android
- **Instrumented tests**: `src/androidTest/kotlin` - need Android framework
- **Robolectric tests**: Marked with `@RunWith(RobolectricTestRunner::class)` for hybrid tests
- **Dependencies**: `libs.bundles.test.common` includes junit, kotlin-test, kotlinx-coroutines-test,
  mockk, turbine

### Common Test Setup

```kotlin
@Before fun setUp() { startKoin { modules(...) } }
@After fun finish() { database.close(); stopKoin() }
// Use StandardTestDispatcher for deterministic async tests
```

---

## Navigation

### Pattern (core:navigation)

- Navigation screens defined as composables in `navigation/screen/` (e.g., `PeopleListScreenNav.kt`)
- Each screen is a sealed class destination + Composable factory
- `HomeNavGraph` orchestrates feature composition and routing
- Features are lazy-composed and scoped via Koin

### Adding a New Screen

1. Define destination in `core:navigation/screen/`
2. Create UI in `feature:*/view/`
3. Register ViewModel + UseCase in `feature:*/di/Module.kt`
4. Include in appropriate NavGraph

---

## Code Conventions

### Naming

- Sealed interfaces for states (not abstract classes)
- `*ViewModel` for state holders
- `*UseCase` for business logic
- `*Screen` for top-level Composables
- `*Module` for Koin module definitions
- Package: `com.mobiledevpro.<module_name>` (replaces underscores with dots)

### Compose Metrics

- Compiler metrics generated to `build/compose_metrics/` (see `build.gradle.kts`)
- Interpreted
  via https://github.com/JetBrains/kotlin/blob/master/plugins/compose/design/compiler-metrics.md
- Helps identify recomposition inefficiencies

### Proguard Rules

- App-level: `app/proguard-rules.pro` (minified release builds)
- Module-level: `*/proguard-rules.pro` + `*/consumer-rules.pro` (for library consumers)

---

## Firebase & Analytics

### Integrated Services

- **Crashlytics**: Enabled in release builds, disabled in debug
- **Analytics**: Via `core:analytics` module, injected as dependency
- **Performance Monitoring**: `firebase-perf` included
- **Firestore**: Via `core:firestore` module
- **Messaging**: FCM support via Firebase

### Debug Analytics Events

```bash
adb shell setprop log.tag.FA VERBOSE
adb shell setprop log.tag.FA-SVC VERBOSE
adb logcat -v time -s FA FA-SVC
```

---

## UI Testing with Maestro

- **Configuration**: `maestro/people-profile-flow.yaml` example
- **Installation**: `curl -Ls "https://get.maestro.mobile.dev" | bash`
- **Run**: `maestro test -c maestro/people-profile-flow.yaml` (emulator only)

---

## Data Persistence

### Room Database (core:database)

- `AppDatabase` singleton managed by Koin
- Schema migrations tracked in `schemas/` directory
- Used by feature modules via UseCase injection

### DataStore (feature:settings_core)

- Preferences stored via `androidx.datastore`
- Protobuf integration for type-safe storage

---

## Common Development Workflows

### Adding a New Feature Module

1. Create `feature/<feature_name>/` directory
2. `build.gradle.kts`: `plugins { id("feature-module") }`
3. Create `src/main/kotlin/com/mobiledevpro/<feature_name>/`:
    - `di/Module.kt` - register ViewModel + dependencies
    - `view/vm/ViewModel.kt` - extends BaseViewModel
    - `view/state/<Feature>UIState.kt` - sealed interface
    - `view/<Feature>Screen.kt` - Composable
    - `domain/usecase/` - business logic
4. Include in `settings.gradle.kts`
5. Register navigation in `core:navigation`

### Refactoring State

1. Update sealed interface in `view/state/<Feature>UIState.kt`
2. Update ViewModel's `observeState()` to emit new states
3. Update Composable UI branch for new state
4. Update ViewModel tests with Turbine

### Debugging

- Check Gradle build issues: `./gradlew assembleDebugDev -x firebase`
- Monitor Compose recompositions: `build/compose_metrics/`
- Inspect Koin DI: Debug breakpoints in `rememberNavViewModel`

---

## Key Files to Know

| File                                                              | Purpose                             |
|-------------------------------------------------------------------|-------------------------------------|
| `gradle/libs.versions.toml`                                       | All dependency versions             |
| `build-logic/src/main/kotlin/*.gradle.kts`                        | Module plugin templates             |
| `core/ui/src/main/kotlin/com/mobiledevpro/ui/vm/BaseViewModel.kt` | MVI base class                      |
| `core:navigation`                                                 | Feature routing & composition       |
| `feature:people_list`                                             | Well-documented example feature     |
| `app/build.gradle.kts`                                            | App configuration, flavors, signing |
| `settings.gradle.kts`                                             | Module includes                     |

---

## References

- MVI Architecture: https://proandroiddev.com
- Jetpack Compose: https://developer.android.com/jetpack/compose
- Koin DI: https://insert-koin.io
- Module Graph Plugin: https://github.com/iurysza/module-graph
- Compose Compiler
  Metrics: https://github.com/JetBrains/kotlin/blob/master/plugins/compose/design/compiler-metrics.md

