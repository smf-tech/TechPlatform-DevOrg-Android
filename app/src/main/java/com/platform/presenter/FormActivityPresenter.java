package com.platform.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.FormRequestCallListener;
import com.platform.listeners.ImageRequestCallListener;
import com.platform.models.forms.Elements;
import com.platform.models.forms.Form;
import com.platform.models.forms.FormData;
import com.platform.models.forms.FormResult;
import com.platform.models.pm.ProcessData;
import com.platform.request.FormRequestCall;
import com.platform.request.ImageRequestCall;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.utility.PlatformGson;
import com.platform.utility.Util;
import com.platform.view.activities.FormActivity;
import com.platform.view.fragments.FormFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings({"FieldCanBeLocal", "CanBeFinal", "unused"})
public class FormActivityPresenter implements FormRequestCallListener,
        ImageRequestCallListener {
    private final String TAG = FormActivityPresenter.class.getName();

    private final Gson gson;
    private FormResult savedForm;
    private WeakReference<FormFragment> formFragment;
    private HashMap<String, String> requestedObject;
    private HashMap<String, List<HashMap<String, String>>> matrixDynamicValuesMap;
    private List<HashMap<String, String>> matrixDynamicValuesList;

    private HashMap<String, List<HashMap<String, String>>> getMatrixDynamicValuesMap() {
        return matrixDynamicValuesMap;
    }

    public void setMatrixDynamicValuesMap(HashMap<String, List<HashMap<String,
            String>>> matrixDynamicValuesMap) {

        this.matrixDynamicValuesMap = matrixDynamicValuesMap;
    }

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

    public void getChoicesByUrl(Elements elements, int pageIndex, int elementIndex,
                                int columnIndex, FormData formData, String url,
                                HashMap<String, String> matrixDynamicInnerMap) {

        FormRequestCall requestCall = new FormRequestCall();
        requestCall.setListener(this);

        formFragment.get().showProgressBar();
        requestCall.getChoicesByUrl(elements, pageIndex, elementIndex, columnIndex,
                formData, url, matrixDynamicInnerMap);
    }

    public void getFormResults(String url) {
        FormRequestCall requestCall = new FormRequestCall();
        requestCall.setListener(this);

        formFragment.get().showProgressBar();
        requestCall.getFormResults(url);
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

        if (formFragment == null || formFragment.get() == null) {
            return;
        }

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
        try {
            if (formFragment != null && formFragment.get() != null) {
                formFragment.get().hideProgressBar();
                AppEvents.trackAppEvent(formFragment.get().getString(R.string.event_form_submitted_fail));
            }

            if (!TextUtils.isEmpty(message)) {
                Log.e(TAG, "onFailureListener :" + message);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        Log.e(TAG, "onErrorListener :" + error);

        if (formFragment != null && formFragment.get() != null) {
            formFragment.get().hideProgressBar();
            AppEvents.trackAppEvent(formFragment.get().getString(R.string.event_form_submitted_fail));

            if (error != null && error.networkResponse != null) {
                if (error.networkResponse.statusCode == 400) {
                    if (error.networkResponse.data!=null) {
                        String json = new String(error.networkResponse.data);
                        json = trimMessage(json);
                        if (json != null) {
                            Util.showToast(json, formFragment.get().getActivity());
                        } else {
                            Util.showToast(Platform.getInstance().getString(R.string.msg_form_duplicate_error),
                                    formFragment.get().getActivity());
                        }
                    } else {
                        Util.showToast(Platform.getInstance().getString(R.string.msg_form_duplicate_error),
                                formFragment.get().getActivity());
                    }
                } else {
                    Util.showToast(formFragment.get().getString(R.string.unexpected_error_occurred), formFragment.get().getActivity());
                    Log.e("onErrorListener",
                            "Unexpected response code " + error.networkResponse.statusCode);
                }
            }
        }
    }

    private String trimMessage(String json) {
        String trimmedString;

        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString("message");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }

        return trimmedString;
    }

    @Override
    public void onFormCreatedUpdated(String message, String requestObjectString, String formId,
                                     String callType, String oid) {

        try {
            JSONObject outerObject = new JSONObject(message);

            if (outerObject.getString("status").equalsIgnoreCase(Constants.ERROR)) {
                if (formFragment != null && formFragment.get() != null) {
                    formFragment.get().hideProgressBar();

                    if (!TextUtils.isEmpty(outerObject.getString("message"))) {
                        Util.showToast(outerObject.getString("message"),
                                formFragment.get().getActivity());
                    } else {
                        Util.showToast(Platform.getInstance().getString(R.string.msg_form_duplicate_error),
                                formFragment.get().getActivity());
                    }
                }
            } else {
                if (formFragment != null && formFragment.get() != null) {
                    formFragment.get().hideProgressBar();
                    Util.showToast(formFragment.get().getResources().getString(R.string.form_submit_success),
                            formFragment.get().getActivity());

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
                            }
                        }

                        FormResult result = new FormResult();
                        result.set_id(idObject.getString(Constants.FormDynamicKeys.OID));
                        result.setFormId(formId);
                        String date = dataObject.getString(Constants.FormDynamicKeys.CREATED_DATE_TIME);
                        result.setCreatedAt(Long.parseLong(date));
                        result.setFormTitle(dataObject.getString(Constants.FormDynamicKeys.FORM_TITLE));
                        result.setResult(requestObject.toString());
                        result.setFormStatus(SyncAdapterUtils.FormStatus.SYNCED);
                        result.setOid(idObject.getString(Constants.FormDynamicKeys.OID));
                        DatabaseManager.getDBInstance(formFragment.get().getContext()).insertFormResult(result);

                        AppEvents.trackAppEvent(formFragment.get().getString(R.string.event_form_submitted_success,
                                dataObject.getString(Constants.FormDynamicKeys.FORM_TITLE)));

                        if (Constants.ONLINE_SUBMIT_FORM_TYPE.equals(callType)) {
                            String countStr = DatabaseManager.getDBInstance(
                                    Objects.requireNonNull(formFragment.get().getContext()))
                                    .getProcessSubmitCount(formId);

                            if (!TextUtils.isEmpty(countStr)) {
                                int count = Integer.parseInt(countStr);
                                DatabaseManager.getDBInstance(
                                        Objects.requireNonNull(formFragment.get().getContext()))
                                        .updateProcessSubmitCount(formId, String.valueOf(++count));
                            }

                            Intent intent = new Intent();
                            if (oid != null) {
                                intent.setAction(SyncAdapterUtils.PARTIAL_FORM_REMOVED);
                            }
                            intent.setAction(SyncAdapterUtils.EVENT_FORM_SUBMITTED);
                            Context context = formFragment.get().getContext();
                            if (context != null) {
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            }
                        }
                    }

                    FormActivity activity = (FormActivity) formFragment.get().getActivity();
                    if (activity != null) {
                        activity.closeScreen(true);
                    }

                    Objects.requireNonNull(formFragment.get().getActivity()).onBackPressed();
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onSuccessListener(String response) {
        if (!TextUtils.isEmpty(response)) {

            Form form = PlatformGson.getPlatformGsonInstance().fromJson(response, Form.class);
            if (form != null && form.getData() != null) {

                FragmentActivity activity = formFragment.get().getActivity();
                if (activity != null) {
                    DatabaseManager.getDBInstance(activity).insertFormSchema(form.getData());
                    Log.d(TAG, "Form schema saved in database.");
                }
            }
        }

        if (formFragment != null && formFragment.get() != null) {
            formFragment.get().hideProgressBar();
            formFragment.get().showNextScreen(response);
        }
    }

    @Override
    public void onChoicesPopulated(String response, Elements elements, int pageIndex, int elementIndex,
                                   int columnIndex, FormData formData, HashMap<String, String> matrixDynamicInnerMap) {

        if (formFragment == null || formFragment.get() == null) {
            return;
        }

        formFragment.get().hideProgressBar();
        if (!TextUtils.isEmpty(response) && formData != null && formFragment != null && formFragment.get() != null) {
            //Fetch form data using formId and update choicesByUrl response path
            String path;
            FormData savedFormData = DatabaseManager.getDBInstance(formFragment.get()
                    .getActivity()).getFormSchema(formData.getId());

            if (columnIndex == -1) {
                //Write choicesByUrl response to internal storage
                path = Util.writeToInternalStorage(Objects.requireNonNull(formFragment.get().getContext()),
                        formData.getId() + "_" + elements.getName(), response);

                savedFormData.getComponents().getPages().get(pageIndex).getElements()
                        .get(elementIndex).setChoicesByUrlResponsePath(path);
            } else {
                //Write choicesByUrl response to internal storage
                path = Util.writeToInternalStorage(Objects.requireNonNull(formFragment.get().getContext()),
                        formData.getId() + "_" + elements.getName()
                                + "_" + elements.getColumns().get(columnIndex).getName(), response);

                savedFormData.getComponents().getPages().get(pageIndex).getElements()
                        .get(elementIndex).getColumns().get(columnIndex).setChoicesByUrlResponsePath(path);
            }

            DatabaseManager.getDBInstance(formFragment.get().getActivity()).updateFormSchema(savedFormData);

            //Update values on UI
            if (columnIndex == -1) {
                elements.setChoicesByUrlResponsePath(path);
                formFragment.get().showChoicesByUrlAsync(response, elements);
            } else {
                formFragment.get().showChoicesByUrlAsyncMD(response,
                        elements.getColumns().get(columnIndex), matrixDynamicInnerMap);
            }
        }
    }

    @Override
    public void onSubmitClick(String submitType, String url, String formId, String oid,
                              final List<Map<String, String>> imageUrlList) {

        FormRequestCall formRequestCall = new FormRequestCall();
        formRequestCall.setListener(this);

        switch (submitType) {
            case Constants.ONLINE_SUBMIT_FORM_TYPE:
                formFragment.get().showProgressBar();
                formRequestCall.createFormResponse(getRequestedObject(), getMatrixDynamicValuesMap(),
                        imageUrlList, url, formId, oid, submitType);
                break;

            case Constants.ONLINE_UPDATE_FORM_TYPE:
                formFragment.get().showProgressBar();
                formRequestCall.updateFormResponse(getRequestedObject(), getMatrixDynamicValuesMap(),
                        imageUrlList, url, formId, oid, submitType);
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
        if (formFragment != null && formFragment.get() != null) {
            formFragment.get().hideProgressBar();
            formFragment.get().getFormDataAndParse(response);
        }
    }

    private void updateFormSubmittedCount(final String formId) {
        if (formFragment != null && formFragment.get() != null) {
            ProcessData processData = DatabaseManager.getDBInstance(formFragment.get().getActivity())
                    .getProcessData(formId);

            String submitCount = processData.getSubmitCount();

            int count = 0;
            if (!TextUtils.isEmpty(submitCount)) {
                count = Integer.parseInt(submitCount);
            }

            count++;
            List<String> formResults = DatabaseManager.getDBInstance(formFragment.get().getActivity())
                    .getAllFormResults(processData.getId());

            if (count == formResults.size()) {
                submitCount = String.valueOf(count);
            } else {
                submitCount = String.valueOf(formResults.size());
            }

            DatabaseManager.getDBInstance(formFragment.get().getActivity())
                    .updateProcessSubmitCount(processData.getId(), submitCount);
        }
    }
}
