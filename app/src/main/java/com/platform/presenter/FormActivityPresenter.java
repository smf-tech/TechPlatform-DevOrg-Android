package com.platform.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.platform.models.LocaleData;
import com.platform.models.forms.Elements;
import com.platform.models.forms.Form;
import com.platform.models.forms.FormData;
import com.platform.models.forms.FormResult;
import com.platform.request.FormRequestCall;
import com.platform.request.ImageRequestCall;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.FormActivity;
import com.platform.view.adapters.LocaleDataAdapter;
import com.platform.view.fragments.FormFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

@SuppressWarnings({"FieldCanBeLocal", "CanBeFinal", "unused"})
public class FormActivityPresenter implements FormRequestCallListener,
        ImageRequestCallListener {
    private final String TAG = FormActivityPresenter.class.getName();

    private final Gson gson;
    private FormResult savedForm;
    private WeakReference<FormFragment> formFragment;
    private HashMap<String, String> requestedObject;

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
                Map<String, String> mUploadedImageUrlList = new HashMap<>();
                mUploadedImageUrlList.put(formName, url);

                formFragment.get().onImageUploaded(mUploadedImageUrlList);
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
            Log.e(TAG, "onFailureListener :" + message);
        }
        AppEvents.trackAppEvent(formFragment.get().getString(R.string.event_form_submitted_fail));
    }

    @Override
    public void onErrorListener(VolleyError error) {
        Log.e(TAG, "onErrorListener :" + error);
        if (formFragment != null && formFragment.get() != null) {
            AppEvents.trackAppEvent(formFragment.get().getString(R.string.event_form_submitted_fail));
            Util.showToast(error.getMessage(), formFragment.get().getActivity());
        }
    }

    @Override
    public void onFormCreatedUpdated(String message, String requestObjectString, String formId, String callType, String oid) {
        Log.e(TAG, "Request succeed " + message);
        Util.showToast(formFragment.get().getResources().getString(R.string.form_submit_success), formFragment.get().getActivity());
        AppEvents.trackAppEvent(formFragment.get().getString(R.string.event_form_submitted_success));

        try {
            JSONObject outerObject = new JSONObject(message);
            JSONObject requestObject = new JSONObject(requestObjectString);

            if (outerObject.has(Constants.RESPONSE_DATA)) {
                JSONObject dataObject = outerObject.getJSONObject(Constants.RESPONSE_DATA);
                JSONObject idObject = dataObject.getJSONObject(Constants.FormDynamicKeys._ID);

                requestObject.put(Constants.FormDynamicKeys._ID, idObject);
                requestObject.put(Constants.FormDynamicKeys.FORM_TITLE,
                        dataObject.getString(Constants.FormDynamicKeys.FORM_TITLE));

                requestObject.put(Constants.FormDynamicKeys.FORM_ID, formId);
                requestObject.put(Constants.FormDynamicKeys.UPDATED_DATE_TIME,
                        dataObject.getString(Constants.FormDynamicKeys.UPDATED_DATE_TIME));

                requestObject.put(Constants.FormDynamicKeys.CREATED_DATE_TIME,
                        dataObject.getString(Constants.FormDynamicKeys.CREATED_DATE_TIME));

                if (oid != null) {
                    FormResult formResult = DatabaseManager
                            .getDBInstance(formFragment.get().getContext()).getFormResult(oid);

                    if (formResult != null) {
                        DatabaseManager.getDBInstance(formFragment.get().getContext())
                                .deleteFormResult(formResult);

                        Intent intent = new Intent(SyncAdapterUtils.PARTIAL_FORM_REMOVED);
                        Context context = formFragment.get().getContext();
                        if (context != null) {
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        }
                    }
                }

                FormResult result = new FormResult();
                result.set_id(idObject.getString(Constants.FormDynamicKeys.OID));
                result.setFormId(formId);
                String date = dataObject.getString(Constants.FormDynamicKeys.CREATED_DATE_TIME);
                result.setFormTitle(dataObject.getString(Constants.FormDynamicKeys.FORM_TITLE));
                result.setResult(requestObject.toString());
                result.setFormStatus(SyncAdapterUtils.FormStatus.SYNCED);
                result.setOid(idObject.getString(Constants.FormDynamicKeys.OID));
                DatabaseManager.getDBInstance(formFragment.get().getContext()).insertFormResult(result);

                switch (callType) {
                    case Constants.ONLINE_SUBMIT_FORM_TYPE:

                        String countStr = DatabaseManager.getDBInstance(
                                Objects.requireNonNull(formFragment.get().getContext()))
                                .getProcessSubmitCount(formId);

                        if (!TextUtils.isEmpty(countStr)) {
                            int count = Integer.parseInt(countStr);
                            DatabaseManager.getDBInstance(
                                    Objects.requireNonNull(formFragment.get().getContext()))
                                    .updateProcessSubmitCount(formId, String.valueOf(++count));
                        }
                        break;

                }
            }

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        if (formFragment.get() != null) {
            FormActivity activity = (FormActivity) formFragment.get().getActivity();
            if (activity != null) {
                activity.closeScreen(true);
            }

            Objects.requireNonNull(formFragment.get().getActivity()).onBackPressed();
        }
    }

    @Override
    public void onSuccessListener(String response) {
        if (!TextUtils.isEmpty(response)) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(LocaleData.class, new LocaleDataAdapter());
            Gson gson = builder.create();

            Form form = gson.fromJson(response, Form.class);
            if (form != null && form.getData() != null) {

                DatabaseManager.getDBInstance(formFragment.get().getActivity()).insertFormSchema(form.getData());
                Log.d(TAG, "Form schema saved in database.");

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
            formFragment.get().showChoicesByUrlAsync(response, elements);
        }
    }

    @Override
    public void onSubmitClick(String submitType, String url, String formId, String oid, final Map<String, String> imageUrlList) {
        FormRequestCall formRequestCall = new FormRequestCall();
        formRequestCall.setListener(this);

        switch (submitType) {
            case Constants.ONLINE_SUBMIT_FORM_TYPE:
                formRequestCall.createFormResponse(getRequestedObject(), imageUrlList, url, formId, oid, submitType);
                break;

            case Constants.ONLINE_UPDATE_FORM_TYPE:
                formRequestCall.updateFormResponse(getRequestedObject(), imageUrlList, url, formId, oid, submitType);
                break;

            case Constants.OFFLINE_SUBMIT_FORM_TYPE:
                DatabaseManager.getDBInstance(formFragment.get().getActivity())
                        .insertFormResult(getSavedForm());

                FormActivity activity = (FormActivity) formFragment.get().getActivity();
                if (activity != null) {
                    activity.closeScreen(true);
                }
                break;

            case Constants.OFFLINE_UPDATE_FORM_TYPE:
                DatabaseManager.getDBInstance(formFragment.get().getActivity())
                        .updateFormResult(getSavedForm());

                activity = (FormActivity) formFragment.get().getActivity();
                if (activity != null) {
                    activity.closeScreen(true);
                }
                break;
        }
    }

    @Override
    public void onFormDetailsLoadedListener(final String response) {
        formFragment.get().hideProgressBar();
        formFragment.get().getFormDataAndParse(response);
    }
}
