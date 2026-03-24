# Poowall - Paywall Removal Redirect App

## Overview

Poowall is a minimal Android app that acts as a URL redirect proxy. When a URL is sent to it (either as a browser intent or via the share sheet), it prepends a paywall removal service URL and opens the result in the user's default browser.

## Behavior

1. **Browser intent**: Registers as a handler for `http://` and `https://` schemes. When selected as the browser for a link, it redirects through the configured paywall removal service.
2. **Share intent**: Accepts `text/plain` shares. Extracts the first URL from the shared text and redirects it.
3. **Settings screen**: A launcher activity with a light indigo theme, organized into sections:
   - **Selection mode** — "Ask every time" toggle switch at the top. When enabled, the service sections below are dimmed and disabled.
   - **Paywall removal service** — radio buttons for the four built-in services in a rounded card.
   - **Custom URL prefix** — text input with its own radio button in a separate pill-shaped card.
   - **Build version** — displayed at the bottom.
4. **Ask mode**: When the "Ask every time" toggle is on, shows an `AlertDialog` picker with all four built-in services before redirecting.
5. **No redirect UI**: The redirect activity uses a dialog theme, is excluded from recents, and finishes immediately after launching the browser intent.
6. **No loop**: Uses `Intent.createChooser()` to open the proxied URL, which prevents the system from routing back into Poowall.

## Supported Services

| Mode | Prefix |
|------|--------|
| removepaywall (default) | `https://removepaywall.com/` |
| removepaywalls | `https://removepaywalls.com/` |
| paywallbuster | `https://paywallbuster.com/` |
| paywallskip | `https://paywallskip.com/` |
| custom | User-defined URL prefix |

The "Ask every time" toggle is independent of service selection — it overlays a picker dialog regardless of which service is selected.

## Settings

Stored in `SharedPreferences` (`poowall_prefs`, `MODE_PRIVATE`):

- **ask_every_time** — boolean toggle for prompting on every link. Default: `false`.
- **mode** — one of `removepaywall`, `removepaywalls`, `paywallbuster`, `paywallskip`, `custom`. Default: `removepaywall`.
- **custom_url** — the custom URL prefix when mode is `custom`.

Legacy `mode=ask` values are migrated to `ask_every_time=true` on first load.

## Technical Details

- **Package**: `com.poowall.app`
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Java**: source/target compatibility Java 8
- **Activities**: `SettingsActivity` (launcher), `RedirectActivity` (handles VIEW + SEND intents)
- **No dependencies** beyond the Android framework
