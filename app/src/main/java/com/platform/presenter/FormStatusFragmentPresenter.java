package com.platform.presenter;

import com.platform.listeners.FormStatusCallListener;
import com.platform.request.FormStatusRequestCall;

public class FormStatusFragmentPresenter {

    private FormStatusCallListener listener;

    public FormStatusFragmentPresenter(FormStatusCallListener listener) {
        this.listener = listener;
    }

    public void getProcessDetails(String processId) {
        FormStatusRequestCall requestCall = new FormStatusRequestCall();
        requestCall.setListener(listener);

        requestCall.getProcessDetails(processId);
    }

    public void setListener(FormStatusCallListener listener) {
        this.listener = listener;
    }
}
