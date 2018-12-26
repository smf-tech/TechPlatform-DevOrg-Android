package com.platform.presenter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.platform.listeners.FormActivityListener;

import java.lang.ref.WeakReference;

public class FormActivityPresenter {

    private Gson gson;
    private WeakReference<FormActivityListener> generalFormActivity;

    public FormActivityPresenter(FormActivityListener activity) {
        this.generalFormActivity = new WeakReference<>(activity);
        this.gson = new GsonBuilder().serializeNulls().create();
    }
}
