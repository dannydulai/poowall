package com.poowall.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class RedirectActivity extends Activity {

    private static final String PROXY_PREFIX = "https://removepaywall.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = extractUrl(getIntent());
        if (url != null) {
            String proxied = PROXY_PREFIX + url;
            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(proxied));
            // Exclude ourselves so the system picks the real default browser
            browser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            browser.addCategory(Intent.CATEGORY_BROWSABLE);

            // Use chooser to avoid looping back into this app
            Intent chooser = Intent.createChooser(browser, null);
            startActivity(chooser);
        }

        finish();
    }

    private String extractUrl(Intent intent) {
        if (intent == null) return null;

        // Opened as a browser (ACTION_VIEW)
        if (Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getData() != null) {
            return intent.getData().toString();
        }

        // Shared via share sheet (ACTION_SEND)
        if (Intent.ACTION_SEND.equals(intent.getAction())) {
            String text = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (text != null) {
                // Extract a URL from the shared text
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
