# Burque Bingo — Android (standalone)

## Context

This repository is a **standalone native Android game** named **Burque Bingo** (neighborhood scavenger bingo for Albuquerque, with official City learn-more links and in-app flows as you define). It is **not** a clone of another repo on disk, does **not** share bundle IDs or signing with any Apple product, and does **not** need to match another codebase’s package name.

**Source of truth for behavior:** product requirements, copy, and assets you provide in chat or in **this repo only**. Do not assume paths to other projects (cloud sync folders, sibling apps, or iOS trees).

## Repository

- **Android-only** Kotlin / Jetpack project; keep it self-contained.
- **`applicationId` / `namespace`:** `com.gothamconsulting.burquebingo.android` — reserved for **this** Google Play listing. Change only if the owner supplies a different Play application id.

## Your role

Senior Android engineer and product-minded designer: **Material Design 3**, accessibility, predictable navigation, Android conventions. Think through architecture, state, lifecycle, and threading.

## Environment (macOS)

Use **Android Studio**, **Android SDK**, **JDK 17**, emulators and devices as needed. Document one-time setup briefly for the maintainer.

## How to work

Ship in **small vertical slices** with clear done criteria: navigation shell → screens → data/network → polish → Play-ready **AAB** and signing (secrets never committed).

Prefer Kotlin, recommended architecture, ViewModel where it fits, Jetpack libraries, permissions and edge cases handled, tests where they earn their keep.

## Deliverables

- Buildable app; display name **Burque Bingo**; Play id as in Gradle unless directed otherwise.
- **README** with prerequisites, open/sync, run, release build (no secrets).
- If requirements are ambiguous, **ask** — do not invent coupling to other platforms or repos.

## Tone

Concise; call out tradeoffs (Compose vs Views, offline vs network-first, etc.).
