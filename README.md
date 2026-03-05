# WatchMemory

A minimal Android app to remember where you stopped watching a show.

## Features
- Track multiple shows with episode numbers and notes
- Quick episode increment from home screen
- Home screen widget showing current episode
- Material 3 design with dark/light mode
- Smooth animations throughout

## Tech Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Architecture:** MVVM
- **Storage:** Room Database
- **Navigation:** Jetpack Navigation Compose
- **Widget:** Glance AppWidget

## Build

```bash
./gradlew assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

## Install

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Structure

```
app/src/main/java/com/example/watchmemory/
├── data/           # Room entity, DAO, database, repository
├── viewmodel/      # Home, Edit, History ViewModels
├── ui/
│   ├── theme/      # Material 3 theme, colors, typography
│   ├── screen/     # Home, Edit, History screens
│   └── navigation/ # NavGraph with slide transitions
├── widget/         # Glance home screen widget
├── MainActivity.kt
└── WatchMemoryApp.kt
```

## Min SDK
API 26 (Android 8.0)
