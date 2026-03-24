package com.poowall.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SettingsActivity extends Activity {

    public static final String PREFS_NAME = "poowall_prefs";
    public static final String KEY_MODE = "mode";
    public static final String KEY_CUSTOM_URL = "custom_url";

    public static final String MODE_REMOVEPAYWALL = "removepaywall";
    public static final String MODE_REMOVEPAYWALLS = "removepaywalls";
    public static final String MODE_PAYWALLBUSTER = "paywallbuster";
    public static final String MODE_PAYWALLSKIP = "paywallskip";
    public static final String MODE_ASK = "ask";
    public static final String MODE_CUSTOM = "custom";

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        RadioGroup radioGroup = findViewById(R.id.radio_group);
        RadioButton radioAsk = findViewById(R.id.radio_ask);
        RadioButton radioCustom = findViewById(R.id.radio_custom);
        View askCard = findViewById(R.id.ask_card);
        EditText customUrlInput = findViewById(R.id.custom_url_input);
        TextView versionText = findViewById(R.id.version_text);

        // Load saved state
        String mode = prefs.getString(KEY_MODE, MODE_REMOVEPAYWALL);
        String customUrl = prefs.getString(KEY_CUSTOM_URL, "");
        customUrlInput.setText(customUrl);

        // Set initial radio selection
        if (MODE_ASK.equals(mode)) {
            radioGroup.clearCheck();
            radioCustom.setChecked(false);
            radioAsk.setChecked(true);
        } else if (MODE_CUSTOM.equals(mode)) {
            radioGroup.clearCheck();
            radioAsk.setChecked(false);
            radioCustom.setChecked(true);
        } else {
            radioAsk.setChecked(false);
            radioCustom.setChecked(false);
            switch (mode) {
                case MODE_REMOVEPAYWALLS:
                    radioGroup.check(R.id.radio_removepaywalls);
                    break;
                case MODE_PAYWALLBUSTER:
                    radioGroup.check(R.id.radio_paywallbuster);
                    break;
                case MODE_PAYWALLSKIP:
                    radioGroup.check(R.id.radio_paywallskip);
                    break;
                default:
                    radioGroup.check(R.id.radio_removepaywall);
                    break;
            }
        }

        // Radio group selection (built-in services)
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == -1) return;
            radioAsk.setChecked(false);
            radioCustom.setChecked(false);
            String newMode;
            if (checkedId == R.id.radio_removepaywalls) newMode = MODE_REMOVEPAYWALLS;
            else if (checkedId == R.id.radio_paywallbuster) newMode = MODE_PAYWALLBUSTER;
            else if (checkedId == R.id.radio_paywallskip) newMode = MODE_PAYWALLSKIP;
            else newMode = MODE_REMOVEPAYWALL;
            prefs.edit().putString(KEY_MODE, newMode).apply();
        });

        // Ask radio button and card (outside RadioGroup, managed manually)
        View.OnClickListener askClickListener = v -> {
            radioGroup.clearCheck();
            radioCustom.setChecked(false);
            radioAsk.setChecked(true);
            prefs.edit().putString(KEY_MODE, MODE_ASK).apply();
            clearFocusAndKeyboard(customUrlInput);
        };
        radioAsk.setOnClickListener(askClickListener);
        askCard.setOnClickListener(askClickListener);

        // Custom radio button (outside RadioGroup, managed manually)
        radioCustom.setOnClickListener(v -> {
            radioGroup.clearCheck();
            radioAsk.setChecked(false);
            radioCustom.setChecked(true);
            prefs.edit().putString(KEY_MODE, MODE_CUSTOM).apply();
            customUrlInput.requestFocus();
        });

        // Custom URL text input
        customUrlInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                prefs.edit().putString(KEY_CUSTOM_URL, s.toString()).apply();
            }
        });

        // Auto-select custom when the input gains focus
        customUrlInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && !radioCustom.isChecked()) {
                radioGroup.clearCheck();
                radioAsk.setChecked(false);
                radioCustom.setChecked(true);
                prefs.edit().putString(KEY_MODE, MODE_CUSTOM).apply();
            }
        });

        // Build version
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionText.setText(pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            versionText.setText("1.0");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View focused = getCurrentFocus();
            if (focused instanceof EditText) {
                int[] loc = new int[2];
                focused.getLocationOnScreen(loc);
                float x = ev.getRawX();
                float y = ev.getRawY();
                if (x < loc[0] || x > loc[0] + focused.getWidth()
                        || y < loc[1] || y > loc[1] + focused.getHeight()) {
                    clearFocusAndKeyboard(focused);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void clearFocusAndKeyboard(View view) {
        view.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
