# Mnote

A single-module Android app for recording expenses and viewing statistics. Built with Jetpack Compose and styled with a neo-brutalist design system.

## Features

- **Expense entry** — a calculator-style keypad for quick amount input, save with a category and optional note
- **Category management** — create, rename, and delete spending categories from the Settings tab
- **Statistics** — filter expenses by date range, then inspect them as either:
  - **Chart** — pie chart breakdown of spending by category (MPAndroidChart)
  - **Table** — itemized list with inline edit and delete
- Default categories (`makan`, `transport`, `main`) are seeded on first launch

## Tech Stack

| Layer | Technology |
|-------|------------|
| UI | Jetpack Compose (Material 3), custom neo-brutalist components |
| Architecture | MVVM — ViewModel → Repository → Room |
| Persistence | Room (KSP-compiled) |
| Navigation | Navigation Compose |
| Charts | MPAndroidChart |
| Language | Kotlin, Java 11 source/target |
| Build | AGP 9.x Groovy DSL with version catalog |

## Project Structure

```
app/src/main/java/com/github/ihyaulhaq/mnote/
├── MainActivity.kt          # Single activity, edge-to-edge
├── MnoteApp.kt              # Application: DB, repository, ViewModel factory
├── data/
│   ├── ExpenseRepository.kt
│   └── local/               # Room entities + DAOs (Expense, Category)
├── viewmodel/               # ExpenseViewModel, CategoryViewModel, StatsViewModel
└── ui/
    ├── components/          # Neo-brutalist primitives (NSurface, NButton, NTextField)
    ├── home/                # HomeScreen (expense input)
    ├── navigation/          # NavHost with home/stats routes
    ├── stats/               # StatsScreen tabs: Chart, Table, Settings
    └── theme/               # NColors palette, MnoteTheme
```

### Design System

All UI uses the neo-brutalist style via `NSurface` (thick borders, hard offset shadows) and the `NColors` palette — colors are never hardcoded. Key palette members: `NColors.Background` (`#FDF2E9`), `NColors.Black` (`#111111`), plus `Blue`, `Red`, `Green`, `Yellow`, `Orange`, and more.

## Building

Prerequisites: JDK 11+ and the Android SDK.

```bash
./gradlew assembleDebug        # build debug APK
./gradlew installDebug         # build + install on connected device/emulator
./gradlew test                 # unit tests
./gradlew connectedAndroidTest # instrumented tests (requires running emulator)
```