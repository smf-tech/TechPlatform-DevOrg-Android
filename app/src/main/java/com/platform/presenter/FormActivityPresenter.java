package com.platform.presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.FormRequestCallListener;
import com.platform.listeners.ImageRequestCallListener;
import com.platform.models.forms.Elements;
import com.platform.models.forms.Form;
import com.platform.models.forms.FormData;
import com.platform.models.forms.FormResult;
import com.platform.request.FormRequestCall;
import com.platform.request.ImageRequestCall;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.fragments.FormFragment;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings({"FieldCanBeLocal", "CanBeFinal", "unused"})
public class FormActivityPresenter implements FormRequestCallListener,
        ImageRequestCallListener {

    private final String TAG = FormActivityPresenter.class.getName();

    private final Gson gson;
    //    private SavedForm savedForm;
    private FormResult savedForm;
    private WeakReference<FormFragment> formFragment;
    private HashMap<String, String> requestedObject;
    private Map<String, String> mUploadedImageUrlList;

    private FormResult getSavedForm() {
        return savedForm;
    }

    public void setSavedForm(FormResult savedForm) {
        this.savedForm = savedForm;
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
        mUploadedImageUrlList = new HashMap<>();
    }

    public void getProcessDetails(String processId) {
        FormRequestCall requestCall = new FormRequestCall();
        requestCall.setListener(this);

        formFragment.get().showProgressBar();
        requestCall.getProcessDetails(processId);
    }

    private void getChoicesByUrl(Elements elements, int pageIndex, int elementIndex, FormData formData) {
        FormRequestCall requestCall = new FormRequestCall();
        requestCall.setListener(this);

        formFragment.get().showProgressBar();
        requestCall.getChoicesByUrl(elements, pageIndex, elementIndex, formData);
    }

    public void getFormResults(String processId) {
        FormRequestCall requestCall = new FormRequestCall();
        requestCall.setListener(this);

        formFragment.get().showProgressBar();
        requestCall.getFormResults(processId);
    }

    @SuppressLint("StaticFieldLeak")
    public void uploadProfileImage(File file, String type, final String formName) {
        ImageRequestCall requestCall = new ImageRequestCall();
        requestCall.setListener(this);

        formFragment.get().showProgressBar();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... voids) {
                requestCall.uploadImageUsingHttpURLEncoded(file, type, formName);
                return null;
            }
        }.execute();

    }

    @Override
    public void onImageUploadedListener(final String response, final String formName) {
        Log.e(TAG, "onImageUploadedListener:\n" + response);

        formFragment.get().hideProgressBar();

        try {
            if (new JSONObject(response).has("data")) {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                String url = (String) data.get("url");
                Log.e(TAG, "onPostExecute: Url: " + url);
                mUploadedImageUrlList.put(formName, url);

            } else {
                Log.e(TAG, "onPostExecute: Invalid response");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
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
    }

    @Override
    public void onFormCreatedUpdated(String message) {
        Log.e(TAG, "Request succeed " + message);
        Util.showToast(formFragment.get().getResources().getString(R.string.form_submit_success),
                formFragment.get().getActivity());
        if (getSavedForm() != null) {
            getSavedForm().setFormStatus(SyncAdapterUtils.FormStatus.SYNCED);
            DatabaseManager.getDBInstance(formFragment.get().getContext()).updateFormResult(getSavedForm());
        }
        Objects.requireNonNull(formFragment.get().getActivity()).onBackPressed();
    }

    @Override
    public void onSuccessListener(String response) {
        if (!TextUtils.isEmpty(response)) {
            Form form = new Gson().fromJson(response, Form.class);
            if (form != null && form.getData() != null) {

                DatabaseManager.getDBInstance(formFragment.get().getActivity()).insertFormSchema(form.getData());
                Log.e(TAG, "Form schema saved in database.");

                //Call choices by url
                if (form.getData().getComponents() != null &&
                        form.getData().getComponents().getPages() != null &&
                        !form.getData().getComponents().getPages().isEmpty()) {
                    for (int pageIndex = 0; pageIndex < form.getData().getComponents().getPages().size(); pageIndex++) {
                        if (form.getData().getComponents().getPages().get(pageIndex).getElements() != null &&
                                !form.getData().getComponents().getPages().get(pageIndex).getElements().isEmpty()) {
                            for (int elementIndex = 0; elementIndex < form.getData().getComponents().getPages().get(pageIndex).getElements().size(); elementIndex++) {
                                if (form.getData().getComponents().getPages().get(pageIndex).getElements().get(elementIndex) != null
                                        && form.getData().getComponents().getPages().get(pageIndex).getElements().get(elementIndex).getChoicesByUrl() != null &&
                                        !TextUtils.isEmpty(form.getData().getComponents().getPages().get(pageIndex).getElements().get(elementIndex).getChoicesByUrl().getUrl()) &&
                                        !TextUtils.isEmpty(form.getData().getComponents().getPages().get(pageIndex).getElements().get(elementIndex).getChoicesByUrl().getTitleName())) {
                                    getChoicesByUrl(form.getData().getComponents().getPages().get(pageIndex).getElements().get(elementIndex), pageIndex, elementIndex, form.getData());
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
    public void onChoicesPopulated(String response, Elements elements, int pageIndex, int elementIndex, FormData formData) {
        if (!TextUtils.isEmpty(response) && formData != null) {
            formData.getComponents().getPages().get(pageIndex).getElements().get(elementIndex).setChoicesByUrlResponse(response);
            DatabaseManager.getDBInstance(formFragment.get().getActivity()).updateFormSchema(formData);
            formFragment.get().showChoicesByUrl(response, elements);
        }
    }

    @Override
    public void onSubmitClick(String submitType, String url) {
        FormRequestCall formRequestCall = new FormRequestCall();
        formRequestCall.setListener(this);

        switch (submitType) {
            case Constants.ONLINE_SUBMIT_FORM_TYPE:
                formRequestCall.createFormResponse(getRequestedObject(), mUploadedImageUrlList, url);
                break;

            case Constants.ONLINE_UPDATE_FORM_TYPE:
                formRequestCall.updateFormResponse(getRequestedObject(), mUploadedImageUrlList, url);
                break;

            case Constants.OFFLINE_SUBMIT_FORM_TYPE:
//                DatabaseManager.getDBInstance(formFragment.get().getActivity())
//                        .insertFormObject(getSavedForm());
                DatabaseManager.getDBInstance(formFragment.get().getActivity())
                        .insertFormResult(getSavedForm());
                Objects.requireNonNull(formFragment.get().getActivity()).onBackPressed();
                break;
            case Constants.OFFLINE_UPDATE_FORM_TYPE:
//                DatabaseManager.getDBInstance(formFragment.get().getActivity())
//                        .updateFormObject(getSavedForm());
                DatabaseManager.getDBInstance(formFragment.get().getActivity())
                        .updateFormResult(getSavedForm());
                Objects.requireNonNull(formFragment.get().getActivity()).onBackPressed();
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
