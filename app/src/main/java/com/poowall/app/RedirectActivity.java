package com.poowall.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

public class RedirectActivity extends Activity {

    private static final String PREFIX_REMOVEPAYWALL = "https://removepaywall.com/";
    private static final String PREFIX_REMOVEPAYWALLS = "https://removepaywalls.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = extractUrl(getIntent());
        if (url == null) {
            finish();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(SettingsActivity.PREFS_NAME, MODE_PRIVATE);
        String mode = prefs.getString(SettingsActivity.KEY_MODE, SettingsActivity.MODE_REMOVEPAYWALL);

        switch (mode) {
            case SettingsActivity.MODE_REMOVEPAYWALLS:
                openProxied(PREFIX_REMOVEPAYWALLS + url);
                break;
            case SettingsActivity.MODE_CUSTOM:
                String custom = prefs.getString(SettingsActivity.KEY_CUSTOM_URL, "");
                openProxied(custom + url);
                break;
            case SettingsActivity.MODE_ASK:
                showPicker(url);
                return; // don't finish yet
            default:
                openProxied(PREFIX_REMOVEPAYWALL + url);
                break;
        }

        finish();
    }

    private void showPicker(String url) {
        String[] options = {"removepaywall.com", "removepaywalls.com"};
        String[] prefixes = {PREFIX_REMOVEPAYWALL, PREFIX_REMOVEPAYWALLS};

        new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
                .setTitle("Choose service")
                .setItems(options, (dialog, which) -> {
                    openProxied(prefixes[which] + url);
                    finish();
                })
                .setOnCancelListener(d -> finish())
                .show();
    }

    private void openProxied(String proxiedUrl) {
        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(proxiedUrl));
        browser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        browser.addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(Intent.createChooser(browser, null));
    }

    private String extractUrl(Intent intent) {
        if (intent == null) return null;

        if (Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getData() != null) {
            return intent.getData().toString();
        }

        if (Intent.ACTION_SEND.equals(intent.getAction())) {
            String text = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (text != null) {
                for (String word : text.split("\\s+")) {
                    if (word.startsWith("http://") || word.startsWith("https://")) {
                        return word;
                    }
                }
            }
        }

        return null;
    }
}
