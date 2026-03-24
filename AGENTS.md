# Poowall - Agent Notes

## Project Structure

```
poowall/
├── app/
│   ├── build.gradle                              # App-level build config
│   └── src/main/
│       ├── AndroidManifest.xml                   # Intent filters for VIEW + SEND, launcher
│       ├── java/com/poowall/app/
│       │   ├── RedirectActivity.java             # Intercepts URLs and redirects through service
│       │   └── SettingsActivity.java             # Launcher activity — service picker UI
│       └── res/
│           ├── color/
│           │   ├── switch_thumb.xml              # Switch thumb color state list
│           │   └── switch_track.xml              # Switch track color state list
│           ├── drawable/
│           │   ├── bg_card_pill.xml              # White pill-shaped card background
│           │   ├── bg_card_pill_bordered.xml     # Pill card with subtle border (build info)
│           │   ├── bg_card_rounded.xml           # White rounded card background
│           │   └── bg_icon_circle.xml            # Circular icon container
│           ├── layout/
│           │   └── activity_settings.xml         # Light-themed sectioned settings layout
│           ├── mipmap-*/
│           │   └── ic_launcher.png               # Launcher icons (mdpi through xxxhdpi)
│           └── values/
│               ├── strings.xml
│               └── themes.xml                    # Light theme with indigo status/nav bars
├── build.gradle                                  # Root build config (AGP 8.2.0)
├── settings.gradle
├── gradle.properties
└── gradle/wrapper/gradle-wrapper.properties
```

## Key Design Decisions

- **Invisible redirect**: `RedirectActivity` uses a dialog theme, `excludeFromRecents`, and `noHistory` so it never appears to the user.
- **Chooser intent**: We use `Intent.createChooser()` instead of raw `ACTION_VIEW` to avoid the system routing the proxied URL back into Poowall (since it also handles http/https).
- **Share sheet support**: In addition to acting as a browser, the app accepts shared text and extracts URLs from it. This covers the case where someone shares an article link from another app.
- **Configurable service**: Users pick from four built-in services or a custom URL prefix. An "Ask every time" toggle overlays a picker dialog independently.
- **Sectioned settings UI**: Light indigo theme with pill/rounded cards, section headers, and a toggle switch for ask mode. The custom URL radio button lives outside the RadioGroup and is managed manually in code.
- **No dependencies**: Pure Android framework, no libraries, no AndroidX.

## Build

```
./gradlew assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`
