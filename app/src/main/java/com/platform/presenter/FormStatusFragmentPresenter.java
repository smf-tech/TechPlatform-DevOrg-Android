package com.platform.presenter;

import com.platform.listeners.FormStatusCallListener;
import com.platform.request.FormStatusRequestCall;
import com.platform.view.fragments.AllFormsFragment;

import java.lang.ref.WeakReference;

@SuppressWarnings("unused")
public class FormStatusFragmentPresenter {

    private FormStatusCallListener listener;
    private WeakReference<AllFormsFragment> allFormsFragment;

    public FormStatusFragmentPresenter(FormStatusCallListener listener) {
        this.listener = listener;
    }

    public FormStatusFragmentPresenter(FormStatusCallListener listener, AllFormsFragment fragment) {
        this.listener = listener;
        this.allFormsFragment = new WeakReference<>(fragment);
    }

    public void getAllProcesses() {
        FormStatusRequestCall requestCall = new FormStatusRequestCall();
        requestCall.setListener(listener);

        if (allFormsFragment != null) {
            allFormsFragment.get().showProgressBar();
        }
        requestCall.getProcesses();
    }

    public void getSubmittedForms(String formId, String url) {
        FormStatusRequestCall requestCall = new FormStatusRequestCall();
        requestCall.setListener(listener);

        if (allFormsFragment != null) {
            allFormsFragment.get().showProgressBar();
        }
        requestCall.getSubmittedForms(formId, url);
    }

    public void setListener(FormStatusCallListener listener) {
        this.listener = listener;
    }
}
