package com.platform.presenter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.platform.view.activities.FormActivity;

import java.lang.ref.WeakReference;

public class FormActivityPresenter {

    private Gson gson;
    private WeakReference<FormActivity> formActivity;

    public FormActivityPresenter(FormActivity activity) {
        this.formActivity = new WeakReference<>(activity);
        this.gson = new GsonBuilder().serializeNulls().create();
    }
}
