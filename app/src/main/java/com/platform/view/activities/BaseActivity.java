package com.platform.view.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.platform.utility.LocaleManager;
import com.platform.utility.Util;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private final String TAG = BaseActivity.class.getSimpleName();

    public BaseActivity() {
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale locale;
        if ("en".equalsIgnoreCase(Util.getLocaleLanguageCode())) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale(Util.getLocaleLanguageCode());
        }

        Context context = LocaleManager.setNewLocale(newBase, locale);
        super.attachBaseContext(context);
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "Destroy called...");
        super.onDestroy();
    }
}
