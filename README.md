# 📺 WATCH MEMORY — NEOBRUTALIST TRACKING ENGINE

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpack-compose&logoColor=white" />
  <img src="https://img.shields.io/badge/Room-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Retrofit-000000?style=for-the-badge&logo=api&logoColor=white" />
</p>

---

## ⚡ REJECT THE SOFTNESS
**Watch Memory** is not just an app; it's a high-impact, neobrutalist-inspired tracking engine. Designed for the obsessed, the dedicated, and those who demand precision. 

Escape the generic, rounded-corner aesthetics of modern UI. **Watch Memory** embraces high contrast, thick black borders, unapologetic vibrant color palettes, and raw structural integrity.

---

## 🍿 CORE CAPABILITIES

### 🔍 IMDB SMART INTEGRATION (New in v3.0)
- **Live Search**: Integrated Free Movie DB API. Type a title and watch the engine fetch metadata in real-time.
- **Metadata Snapping**: Automatically populates IMDB IDs, release years, and types.
- **Visual Evidence**: Fetches high-resolution movie posters directly into your feed and widget.

### 🏠 DYNAMIC HOME WIDGETS
- **Instant Access**: Track progress directly from your home screen.
- **Poster Support**: High-performance bitmap delivery ensures posters render beautifully on your home screen.
- **Fast Increment**: One-tap progress updates without even opening the app.

### ⚙️ TRACKING ENGINE
- **Cinema Mode**: Specialized time-based tracking (minutes) for movies.
- **Serialized Content**: Classic episode steppers for Anime and TV Series.
- **Status Automation**: Cards automatically transition to "Completed" and "Rewatch" states based on your progress.

---

## 🛡️ SECURITY & PRIVACY BY DESIGN
Your data belongs to you. Period.

- **Local-First Architecture**: All tracking data, user profiles, and notes are stored strictly on-device using a Room SQLite database.
- **Zero Cloud Leakage**: No external servers handle your personal information.
- **Secure Networking**: All IMDB metadata fetching is performed over standard encrypted **HTTPS** protocols.
- **Minimal Permissions**: We only ask for `INTERNET` permission to pull movie data. No location, no contacts, no tracking.

---

## 🔨 BUILD THE VOID

### Compilation
```bash
./gradlew assembleDebug
```

### Installation
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---
**Project of halfthew0rldaway**  
*Built for the aesthetic. Optimized for the data.*
