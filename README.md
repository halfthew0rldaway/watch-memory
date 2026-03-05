# Watch Memory

Version 2.0.0

Watch Memory is a high-performance, neobrutalist-inspired tracking engine for Android, designed for users who value precision and aesthetic intensity. This application provides a streamlined interface for monitoring progress across Anime, Series, and Cinema with zero friction.

## Core Philosophy
The application adheres to the Neobrutalist design movement: high contrast, bold borders, vibrant accents, and raw structural integrity. It rejects the softness of modern UI in favor of a sharp, tactile experience that feels alive under every interaction.

## Version 2.0 Feature Set

### Advanced Category Logic
- Cinema Mode: Specialized time-based tracking for movies. Track your progress in minutes (e.g., 45/120 min) rather than episodes.
- Serialized Content: Classic episode-based steppers optimized for Anime and TV Series.
- Smart Logic: Automatic transition to "Rewatch" state upon completion, allowing for infinite viewing loops.

### Command and Control
- Dynamic Filtering: Pivot your feed between All, Watching, and Completed shows using a tactile pill-based interface.
- Advanced Sorting: Organize your archive alphabetically (A-Z) or chronologically (Latest Update).
- Global Search: High-speed search interface to find any show in your collection instantly.

### Identity and Customization
- The Title System: Express your personality with selectable neobrutalist titles such as Void Observer, Master Collector, or Data Core.
- Persistent Preferences: User identification and titles are stored securely via Jetpack DataStore for a consistent experience.

### Technical Excellence
- Reactive UI: Built entirely with Jetpack Compose using spring-based physics for all animations.
- Robust Storage: Powered by Room Database for local-first, offline-capable data management.
- Modern Architecture: Strict MVVM implementation ensures a clean separation of concerns and maintainable logic.

## Technical Specifications
- Language: Kotlin
- UI Framework: Jetpack Compose
- Target SDK: 34
- Minimum SDK: 26 (Android 8.0)
- Local Storage: Room + SQLite
- Preference Management: Jetpack DataStore

## Build and Deployment

### Compilation
To generate a debug build of the application:
```bash
./gradlew assembleDebug
```

### Artifact Location
The resulting APK can be found at:
`app/build/outputs/apk/debug/app-debug.apk`

### Installation
Use the Android Debug Bridge (ADB) to install the artifact on a connected device:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---
Project maintained by halfthew0rldaway
Built for the Void.
