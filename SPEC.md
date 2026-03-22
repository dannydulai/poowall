# Poowall - Paywall Removal Redirect App

## Overview

Poowall is a minimal, non-interactive Android app that acts as a URL redirect proxy. When a URL is sent to it (either as a browser intent or via the share sheet), it prepends `https://removepaywall.com/` to the URL and opens it in the user's default browser.

## Behavior

1. **Browser intent**: Registers as a handler for `http://` and `https://` schemes. When selected as the browser for a link, it redirects through removepaywall.com.
2. **Share intent**: Accepts `text/plain` shares. Extracts the first URL from the shared text and redirects it.
3. **No UI**: Uses `Theme.NoDisplay` — the activity is invisible, finishes immediately after launching the redirect.
4. **No loop**: Uses `Intent.createChooser()` to open the proxied URL, which prevents the system from routing back into Poowall.

## Technical Details

- **Package**: `com.poowall.app`
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Single activity**: `RedirectActivity` — handles both VIEW and SEND intents
- **No dependencies** beyond the Android framework
