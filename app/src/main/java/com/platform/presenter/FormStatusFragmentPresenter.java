package com.platform.presenter;

import com.platform.listeners.FormStatusCallListener;
import com.platform.request.FormStatusRequestCall;
import com.platform.view.fragments.AllFormsFragment;

import java.lang.ref.WeakReference;

@SuppressWarnings("unused")
public class FormStatusFragmentPresenter {

    private FormStatusCallListener listener;
    private WeakReference<AllFormsFragment> mAllFormsFragment;

    public FormStatusFragmentPresenter(FormStatusCallListener listener, AllFormsFragment fragment) {
        this.listener = listener;
        this.mAllFormsFragment = new WeakReference<>(fragment);
    }

    public FormStatusFragmentPresenter(FormStatusCallListener listener) {
        this.listener = listener;
    }

    public void getAllProcesses() {
        FormStatusRequestCall requestCall = new FormStatusRequestCall();
        requestCall.setListener(listener);

        if (mAllFormsFragment != null) {
            mAllFormsFragment.get().showProgressBar();
        }
        requestCall.getProcesses();
    }

    public void getSubmittedForms(String formId, String url) {
        FormStatusRequestCall requestCall = new FormStatusRequestCall();
        requestCall.setListener(listener);

        if (mAllFormsFragment != null) {
            mAllFormsFragment.get().showProgressBar();
        }
        requestCall.getSubmittedForms(formId, url);
    }

    public void setListener(FormStatusCallListener listener) {
        this.listener = listener;
    }
}
