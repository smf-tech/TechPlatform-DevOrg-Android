package com.platform.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.platform.database.DatabaseManager;
import com.platform.listeners.FormRequestCallListener;
import com.platform.models.SavedForm;
import com.platform.models.forms.Elements;
import com.platform.models.forms.Form;
import com.platform.models.forms.Page;
import com.platform.request.FormRequestCall;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.fragments.FormFragment;

import java.lang.ref.WeakReference;
import java.util.HashMap;

@SuppressWarnings({"FieldCanBeLocal", "CanBeFinal", "unused"})
public class FormActivityPresenter implements FormRequestCallListener {

    private final String TAG = FormActivityPresenter.class.getName();

    private final Gson gson;
    private String formId;
    private SavedForm savedForm;
    private WeakReference<FormFragment> formFragment;
    private HashMap<String, String> requestedObject;

    private SavedForm getSavedForm() {
        return savedForm;
    }

    public void setSavedForm(SavedForm savedForm) {
        this.savedForm = savedForm;
    }

    private String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    private HashMap<String, String> getRequestedObject() {
        return requestedObject;
    }

    public void setRequestedObject(HashMap<String, String> requestedObject) {
        this.requestedObject = requestedObject;
    }

    public FormActivityPresenter(FormFragment fragment) {
        this.formFragment = new WeakReference<>(fragment);
        this.gson = new GsonBuilder().serializeNulls().create();
    }

    public void getProcessDetails(String processId) {
        FormRequestCall requestCall = new FormRequestCall();
        requestCall.setListener(this);

        formFragment.get().showProgressBar();
        requestCall.getProcessDetails(processId);
    }

    private void getChoicesByUrl(String choicesUrl) {
        FormRequestCall requestCall = new FormRequestCall();
        requestCall.setListener(this);

        formFragment.get().showProgressBar();
        requestCall.getChoicesByUrl(choicesUrl);
    }

    public void getFormResults(String processId) {
        FormRequestCall requestCall = new FormRequestCall();
        requestCall.setListener(this);

        formFragment.get().showProgressBar();
        requestCall.getFormResults(processId);
    }

    @Override
    public void onFailureListener(String message) {
        if (!TextUtils.isEmpty(message)) {
            Log.e(TAG, "Request failed :" + message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        Log.e(TAG, "Request Error :" + error);
        /*if (formFragment != null && formFragment.get() != null &&
                formFragment.get().getActivity() != null) {
            DatabaseManager.getDBInstance(formFragment.get().getActivity()).insertFormObject(getSavedForm());
        }*/
    }

    @Override
    public void onFormCreated(String message) {
        Log.e(TAG, "Request succeed " + message);
        Util.showToast("Form submitted successfully", formFragment.get().getActivity());
    }

    @Override
    public void onSuccessListener(String response) {
        if (!TextUtils.isEmpty(response)) {
            Log.e(TAG, "Process Details " + response);
            Form form = new Gson().fromJson(response, Form.class);
            if (form != null && form.getData() != null) {
                DatabaseManager.getDBInstance(formFragment.get().getActivity()).insertFormSchema(form.getData());
                Log.e(TAG, "Form schema saved in database.");

                //Call choices by url
                if (form.getData().getComponents() != null &&
                        form.getData().getComponents().getPages() != null &&
                        !form.getData().getComponents().getPages().isEmpty()) {
                    for (Page page :
                            form.getData().getComponents().getPages()) {
                        if (page.getElements() != null && !page.getElements().isEmpty()) {
                            for (Elements elements :
                                    page.getElements()) {
                                if (elements != null && elements.getChoicesByUrl() != null &&
                                        !TextUtils.isEmpty(elements.getChoicesByUrl().getUrl())) {
                                    getChoicesByUrl(elements.getChoicesByUrl().getUrl());
                                }
                            }
                        }
                    }
                }
            }
        }

        formFragment.get().hideProgressBar();
        formFragment.get().showNextScreen(response);
    }

    @Override
    public void onSubmitClick(String submitType) {
        switch (submitType) {
            case Constants.ONLINE_SUBMIT_FORM_TYPE:
                FormRequestCall formRequestCall = new FormRequestCall();
                formRequestCall.setListener(this);

                formRequestCall.createFormResponse(getFormId(), getRequestedObject());
                break;

            case Constants.OFFLINE_SUBMIT_FORM_TYPE:
                DatabaseManager.getDBInstance(formFragment.get().getActivity()).insertFormObject(getSavedForm());
                break;
        }
    }

    @Override
    public void onFormDetailsLoadedListener(final String response) {
        Log.e(TAG, "Form Details\n" + response);

        formFragment.get().hideProgressBar();
        formFragment.get().getFormDataAndParse(response);
    }
}
