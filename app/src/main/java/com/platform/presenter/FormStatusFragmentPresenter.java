package com.platform.presenter;

import com.platform.listeners.FormStatusCallListener;
import com.platform.request.FormStatusRequestCall;

@SuppressWarnings("unused")
public class FormStatusFragmentPresenter {

    private FormStatusCallListener listener;

    public FormStatusFragmentPresenter(FormStatusCallListener listener) {
        this.listener = listener;
    }

    public void getAllFormMasters() {
        FormStatusRequestCall requestCall = new FormStatusRequestCall();
        requestCall.setListener(listener);

        requestCall.getFormMasters();
    }

    public void getSubmittedFormsOfMaster(String formId) {
        FormStatusRequestCall requestCall = new FormStatusRequestCall();
        requestCall.setListener(listener);

        requestCall.getSubmittedFormsOfMaster(formId);
    }

    public void setListener(FormStatusCallListener listener) {
        this.listener = listener;
    }
}
