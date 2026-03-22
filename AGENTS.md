# Poowall - Agent Notes

## Project Structure

```
poowall/
├── app/
│   ├── build.gradle                          # App-level build config
│   └── src/main/
│       ├── AndroidManifest.xml               # Intent filters for VIEW + SEND
│       ├── java/com/poowall/app/
│       │   └── RedirectActivity.java         # The only activity — does the redirect
│       └── res/values/
│           ├── strings.xml
│           └── themes.xml
├── build.gradle                              # Root build config
├── settings.gradle
├── gradle.properties
└── gradle/wrapper/gradle-wrapper.properties
```

## Key Design Decisions

- **No UI at all**: `Theme.NoDisplay` + `finish()` immediately after launching the intent. The app is invisible.
- **Chooser intent**: We use `Intent.createChooser()` instead of raw `ACTION_VIEW` to avoid the system routing the proxied URL back into Poowall (since it also handles http/https).
- **Share sheet support**: In addition to acting as a browser, the app accepts shared text and extracts URLs from it. This covers the case where someone shares an article link from another app.
- **No dependencies**: Pure Android framework, no libraries, no AndroidX.

## Build

```
./gradlew assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`
