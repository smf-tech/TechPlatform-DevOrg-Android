package com.platform.presenter;

import com.platform.listeners.FormStatusCallListener;
import com.platform.request.FormStatusRequestCall;

@SuppressWarnings("unused")
public class FormStatusFragmentPresenter {

    private FormStatusCallListener listener;

    public FormStatusFragmentPresenter(FormStatusCallListener listener) {
        this.listener = listener;
    }

    public void getAllProcesses() {
        FormStatusRequestCall requestCall = new FormStatusRequestCall();
        requestCall.setListener(listener);

        requestCall.getAllProcesses();
    }

    public void setListener(FormStatusCallListener listener) {
        this.listener = listener;
    }
}
