package com.platform.services;

import android.util.Log;

import com.platform.listeners.FormFragmentListener;

import java.lang.ref.WeakReference;

@SuppressWarnings("ConstantConditions")
public class FormDataSubmit extends Thread {

    private final String TAG = FormDataSubmit.class.getName();
    private final WeakReference<FormFragmentListener> listener;

    public FormDataSubmit(FormFragmentListener listener) {
        this.listener = new WeakReference<>(listener);
    }

    @Override
    public void run() {
        super.run();
        try {
            getDataFromForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDataFromForm() {
        if (listener == null || listener.get() == null) {
            return;
        }

        listener.get().getRequest();
        Log.i(TAG, "Collect all form data");
    }
}
