package com.platform.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.platform.listeners.GeneralFormActivityListener;

import java.lang.ref.WeakReference;

public class GeneralFormActivityController {

    private Gson gson;
    private WeakReference<GeneralFormActivityListener> generalFormActivity;

    public GeneralFormActivityController(GeneralFormActivityListener activity) {
        this.generalFormActivity = new WeakReference<>(activity);
        this.gson = new GsonBuilder().serializeNulls().create();
    }
}
