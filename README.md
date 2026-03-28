# Burque Bingo (Android)

Standalone **native Kotlin / Jetpack Compose** app. This tree is intended to live on **local disk** (for example under your home folder), not inside cloud-synced developer roots, so Gradle and the Android SDK behave predictably.

- **Application id:** `com.cabq.burquebingo.android` (its own Play Console app; not shared with any iOS or other Android variant unless you change it deliberately).
- **Project location example:** `/Users/davidpiersol/burque-bingo-android`

## Prerequisites

- **Android Studio** (recent stable) — brings the **Android SDK** and emulator tooling.
- **JDK 17** for Gradle (Android Studio can use its bundled runtime for the IDE).

## Open and run

1. **Android Studio** → **Open** → choose this folder (`burque-bingo-android`).
2. Let **Gradle sync** finish; install any suggested SDK packages.
3. Run on an **AVD** or a USB device with **USB debugging** enabled.

Or from a terminal (after `local.properties` exists or `ANDROID_HOME` is set):

```bash
cd /Users/davidpiersol/burque-bingo-android
./gradlew :app:installDebug
```

## Command-line SDK path

Create `local.properties` in the project root (gitignored):

```properties
sdk.dir=/Users/davidpiersol/Library/Android/sdk
```

Adjust if your SDK is installed elsewhere.

## Release build

1. Add a **release signing** config in `app/build.gradle.kts` (keystore path and credentials via env or `local.properties` — **never** commit keys or passwords).
2. Build an **AAB** for Play:

```bash
./gradlew :app:bundleRelease
```

Output: `app/build/outputs/bundle/release/app-release.aab`.

## Agent / Cursor context

See [AGENTS.md](./AGENTS.md).
