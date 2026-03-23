# Poowall

A tiny Android app that redirects URLs through paywall removal services. Select it as your browser or share a URL to it — it prepends the removal service URL and opens the result in your real browser.

## Features

- Appears as a browser option when tapping links
- Accepts URLs via the share sheet
- Configurable service: [removepaywall.com](https://removepaywall.com), [removepaywalls.com](https://removepaywalls.com), [paywallbuster.com](https://paywallbuster.com), [paywallskip.com](https://paywallskip.com), ask every time, or a custom URL prefix
- No UI beyond the settings screen — redirects are instant and invisible

## Build

```
./gradlew assembleRelease
```

APK output: `app/build/outputs/apk/release/app-release-unsigned.apk`

Requires JDK 17 and the Android SDK (API 34).

## Install

```
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Usage

1. Open Poowall from your app drawer to choose your preferred paywall removal service
2. Tap any link anywhere on your phone and pick **Poowall** from the browser list — or share a URL to it from any app
