package com.poowall.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SettingsActivity extends Activity {

    public static final String PREFS_NAME = "poowall_prefs";
    public static final String KEY_MODE = "mode";
    public static final String KEY_CUSTOM_URL = "custom_url";

    public static final String MODE_REMOVEPAYWALL = "removepaywall";
    public static final String MODE_REMOVEPAYWALLS = "removepaywalls";
    public static final String MODE_ASK = "ask";
    public static final String MODE_CUSTOM = "custom";

    private SharedPreferences prefs;
    private EditText customUrlInput;
    private TextView statusText;
    private Handler handler = new Handler();
    private Runnable hideStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        customUrlInput = findViewById(R.id.custom_url_input);
        statusText = findViewById(R.id.status_text);

        // Load saved state
        String mode = prefs.getString(KEY_MODE, MODE_REMOVEPAYWALL);
        String customUrl = prefs.getString(KEY_CUSTOM_URL, "");
        customUrlInput.setText(customUrl);

        switch (mode) {
            case MODE_REMOVEPAYWALLS:
                radioGroup.check(R.id.radio_removepaywalls);
                break;
            case MODE_ASK:
                radioGroup.check(R.id.radio_ask);
                break;
            case MODE_CUSTOM:
                radioGroup.check(R.id.radio_custom);
                customUrlInput.setVisibility(View.VISIBLE);
                break;
            default:
                radioGroup.check(R.id.radio_removepaywall);
                break;
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String newMode;
            if (checkedId == R.id.radio_removepaywall) {
                newMode = MODE_REMOVEPAYWALL;
                customUrlInput.setVisibility(View.GONE);
            } else if (checkedId == R.id.radio_removepaywalls) {
                newMode = MODE_REMOVEPAYWALLS;
                customUrlInput.setVisibility(View.GONE);
            } else if (checkedId == R.id.radio_ask) {
                newMode = MODE_ASK;
                customUrlInput.setVisibility(View.GONE);
            } else {
                newMode = MODE_CUSTOM;
                customUrlInput.setVisibility(View.VISIBLE);
                customUrlInput.requestFocus();
            }
            prefs.edit().putString(KEY_MODE, newMode).apply();
            showSaved();
        });

        customUrlInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                prefs.edit().putString(KEY_CUSTOM_URL, s.toString()).apply();
                showSaved();
            }
        });
    }

    private void showSaved() {
        statusText.setVisibility(View.VISIBLE);
        if (hideStatus != null) handler.removeCallbacks(hideStatus);
        hideStatus = () -> statusText.setVisibility(View.INVISIBLE);
        handler.postDelayed(hideStatus, 1500);
    }
}
