# Burque Bingo (Android)

Standalone **native Kotlin / Jetpack Compose** app. This tree is intended to live on **local disk** (for example under your home folder), not inside cloud-synced developer roots, so Gradle and the Android SDK behave predictably.

- **Application id:** `com.cabq.burquebingo.android` (its own Play Console app; not shared with any iOS or other Android variant unless you change it deliberately).
- **Project location example:** `/Users/davidpiersol/burque-bingo-android`

## Prerequisites (this Mac)

Installed via **Homebrew** (CLI-friendly) plus the **Android Studio** app:

| Piece | Role |
|--------|------|
| `brew install openjdk@17` | JDK for Gradle (`JAVA_HOME` in `~/.zshrc`) |
| `brew install --cask android-commandlinetools` | SDK, `sdkmanager`, `avdmanager`, `adb` |
| `brew install --cask android-studio` | IDE, Device Manager, graphical emulator |

**SDK location (Homebrew):** `/opt/homebrew/share/android-commandlinetools`  
**Emulator AVD:** `BurqueBingo_API35` (Pixel 8, Android 35, Google APIs, arm64)

After opening a new terminal, `java`, `adb`, and `sdkmanager` should work (see `~/.zshrc`).

## Open and run

1. **Android Studio** → **Open** → choose this folder (`burque-bingo-android`).  
   If Studio asks for an SDK path, set it to `/opt/homebrew/share/android-commandlinetools` (or rely on `local.properties` below).
2. Let **Gradle sync** finish.
3. Start the **BurqueBingo_API35** virtual device (Device Manager), or connect a USB device with **USB debugging**.

**Terminal — build and install** (emulator running or device attached):

```bash
cd /Users/davidpiersol/burque-bingo-android
./gradlew :app:installDebug
```

**Terminal — start emulator only:**

```bash
emulator -avd BurqueBingo_API35
```

## `local.properties` (SDK path)

This repo may include a gitignored `local.properties` pointing at the Homebrew SDK:

```properties
sdk.dir=/opt/homebrew/share/android-commandlinetools
```

If you use Android Studio’s default SDK instead, it is often `sdk.dir=/Users/YOU/Library/Android/sdk`.

## Release build

1. Add a **release signing** config in `app/build.gradle.kts` (keystore path and credentials via env or `local.properties` — **never** commit keys or passwords).
2. Build an **AAB** for Play:

```bash
./gradlew :app:bundleRelease
```

Output: `app/build/outputs/bundle/release/app-release.aab`.

## Agent / Cursor context

See [AGENTS.md](./AGENTS.md).
