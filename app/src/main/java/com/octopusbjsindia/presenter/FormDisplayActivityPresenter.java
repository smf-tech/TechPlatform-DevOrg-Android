package com.octopusbjsindia.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.listeners.FormRequestCallListener;
import com.octopusbjsindia.listeners.ImageRequestCallListener;
import com.octopusbjsindia.models.forms.Elements;
import com.octopusbjsindia.models.forms.Form;
import com.octopusbjsindia.models.forms.FormData;
import com.octopusbjsindia.models.forms.FormResult;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.request.FormRequestCall;
import com.octopusbjsindia.request.ImageRequestCall;
import com.octopusbjsindia.syncAdapter.SyncAdapterUtils;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.FormDisplayActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FormDisplayActivityPresenter implements APIPresenterListener, FormRequestCallListener, ImageRequestCallListener {
    private WeakReference<FormDisplayActivity> fragmentWeakReference;
    private final String TAG = FormDisplayActivity.class.getName();
    public static final String GET_FORM_SCHEMA = "getFormSchema";
    private final Gson gson;
    private FormResult savedForm;
    private HashMap<String, String> requestedObject;
    private HashMap<String, List<HashMap<String, String>>> matrixDynamicValuesMap;

    public FormDisplayActivityPresenter(FormDisplayActivity tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
        this.gson = new GsonBuilder().serializeNulls().create();
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    private HashMap<String, String> getRequestedObject() {
        return requestedObject;
    }

    public void setRequestedObject(HashMap<String, String> requestedObject) {
        this.requestedObject = requestedObject;
    }

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

    public void getFormSchema(String formId) {
        fragmentWeakReference.get().showProgressBar();
//        final String getFormSchemaUrl = "http://api.dxsurvey.com/api/Survey/getSurvey?surveyId=d8b0f086-39b0-43ca-b3de-964af845eb31";
        final String getFormSchemaUrl = BuildConfig.BASE_URL + String.format(Urls.PM.GET_PROCESS_DETAILS, formId);
        Log.d(TAG, "getFormSchemaUrl: url" + getFormSchemaUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_FORM_SCHEMA, getFormSchemaUrl);
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            fragmentWeakReference.get().onFailureListener(requestID, message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            if (error != null) {
                fragmentWeakReference.get().onErrorListener(requestID, error);
            }
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (fragmentWeakReference == null) {
            return;
        }
        fragmentWeakReference.get().hideProgressBar();
        if (response != null) {
            if (requestID.equalsIgnoreCase(FormDisplayActivityPresenter.GET_FORM_SCHEMA)) {
                try {
                    Form form = PlatformGson.getPlatformGsonInstance().fromJson(response, Form.class);
                    form.getData().setJurisdictions_(gson.toJson(form.getData().getJurisdictions()));
                    if (form != null && form.getData() != null) {
                        FormDisplayActivity activity = fragmentWeakReference.get();
                        if (activity != null) {
                            DatabaseManager.getDBInstance(activity).insertFormSchema(form.getData());
                        }
                    }
                    fragmentWeakReference.get().parseFormSchema(form.getData());
//                    Gson gson = new Gson();
//                    Components components = gson.fromJson(response,
//                            Components.class);
//                    fragmentWeakReference.get().parseFormSchema(components);

                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
                }
            }
        }
    }

    @Override
    public void onImageUploadedListener(String response, String formName) {
        Log.e(TAG, "onImageUploadedListener:\n" + response);

        fragmentWeakReference.get().hideProgressBar();

        try {
            if (new JSONObject(response).has("data")) {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                String url = (String) data.get("url");
                Log.e(TAG, "onPostExecute: Url: " + url);
                Map<String, String> mUploadedImageUrlList = new HashMap<>();
                mUploadedImageUrlList.put(formName, url);

                fragmentWeakReference.get().onImageUploaded(mUploadedImageUrlList);
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
            if (fragmentWeakReference != null && fragmentWeakReference != null) {
                fragmentWeakReference.get().hideProgressBar();

                //fragmentWeakReference.get().enableSubmitButton(true);
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

        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();

            if (error != null && error.networkResponse != null) {
                if (error.networkResponse.statusCode == 400) {
                    if (error.networkResponse.data != null) {
                        String json = new String(error.networkResponse.data);
                        json = Util.trimMessage(json);
                        if (json != null) {
                            Util.showToast(json, fragmentWeakReference.get());
                        } else {
                            Util.showToast(Platform.getInstance().getString(R.string.msg_form_duplicate_error),
                                    fragmentWeakReference.get());
                        }
                    } else {
                        Util.showToast(Platform.getInstance().getString(R.string.msg_form_duplicate_error),
                                fragmentWeakReference.get());
                    }
                } else if (error.networkResponse.statusCode == Constants.TIMEOUT_ERROR_CODE) {
                    if (error.networkResponse.data != null) {
                        String json = new String(error.networkResponse.data);
                        json = Util.trimMessage(json);
                        if (json != null) {
                            Util.showToast(json, fragmentWeakReference.get());
                        } else {
                            Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                                    fragmentWeakReference.get());
                        }
                    } else {
                        Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                                fragmentWeakReference.get());
                    }
                } else {
                    Util.showToast(fragmentWeakReference.get().getString(R.string.unexpected_error_occurred), fragmentWeakReference.get());
                    Log.e("onErrorListener",
                            "Unexpected response code " + error.networkResponse.statusCode);
                }
            } else {
                Util.showToast(fragmentWeakReference.get().getString(R.string.unexpected_error_occurred), fragmentWeakReference.get());
            }

            //fragmentWeakReference.get().enableSubmitButton(true);
        }
    }

    @Override
    public void onFormCreatedUpdated(String message, String requestObjectString, String formId, String callType, @Nullable String oid) {
        try {
            JSONObject outerObject = new JSONObject(message);

            if (outerObject.getString("status").equalsIgnoreCase(Constants.ERROR)) {
                if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
                    fragmentWeakReference.get().hideProgressBar();

                    if (!TextUtils.isEmpty(outerObject.getString("message"))) {
                        Util.showToast(outerObject.getString("message"),
                                fragmentWeakReference.get());
                    } else {
                        Util.showToast(Platform.getInstance().getString(R.string.msg_form_duplicate_error),
                                fragmentWeakReference.get());
                    }

                    //fragmentWeakReference.get().enableSubmitButton(true);
                }
            } else {
                if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
                    fragmentWeakReference.get().hideProgressBar();
                    Util.showToast(fragmentWeakReference.get().getResources().getString(R.string.form_submit_success),
                            fragmentWeakReference.get());

                    JSONObject requestObject = new JSONObject(requestObjectString);

                    if (outerObject.has(Constants.RESPONSE_DATA)) {
                        JSONObject dataObject = outerObject.getJSONObject(Constants.RESPONSE_DATA);
                        JSONObject idObject = dataObject.getJSONObject(Constants.FormDynamicKeys._ID);

//                        requestObject.put(Constants.FormDynamicKeys._ID, idObject);
//                        requestObject.put(Constants.FormDynamicKeys.FORM_TITLE,
//                                dataObject.getString(Constants.FormDynamicKeys.FORM_TITLE));
//
//                        requestObject.put(Constants.FormDynamicKeys.FORM_ID, formId);
//                        requestObject.put(Constants.FormDynamicKeys.UPDATED_DATE_TIME,
//                                dataObject.getString(Constants.FormDynamicKeys.UPDATED_DATE_TIME));
//
//                        requestObject.put(Constants.FormDynamicKeys.CREATED_DATE_TIME,
//                                dataObject.getString(Constants.FormDynamicKeys.CREATED_DATE_TIME));

                        if (oid != null) {
                            FormResult formResult = DatabaseManager
                                    .getDBInstance(fragmentWeakReference.get()).getFormResult(oid);

                            if (formResult != null) {
                                DatabaseManager.getDBInstance(fragmentWeakReference.get())
                                        .deleteFormResult(formResult);
                            }
                        }

                        FormResult result = new FormResult();
                        result.set_id(idObject.getString(Constants.FormDynamicKeys.OID));
                        result.setFormId(formId);
//                        String date = dataObject.getString(Constants.FormDynamicKeys.CREATED_DATE_TIME);
//                        result.setCreatedAt(Long.parseLong(date));
                        String updatedDate = dataObject.getString(Constants.FormDynamicKeys.UPDATED_DATE_TIME);
                        result.setCreatedAt(Long.parseLong(updatedDate));
                        result.setFormTitle(dataObject.getString(Constants.FormDynamicKeys.FORM_TITLE));
                        result.setResult(requestObject.toString());
                        result.setFormStatus(SyncAdapterUtils.FormStatus.SYNCED);
                        result.setOid(idObject.getString(Constants.FormDynamicKeys.OID));
                        result.setFormApprovalStatus(dataObject.getString(Constants.FormDynamicKeys.STATUS));

                        DatabaseManager.getDBInstance(fragmentWeakReference.get()).insertFormResult(result);

                        if (Constants.ONLINE_SUBMIT_FORM_TYPE.equals(callType)) {
                            String countStr = DatabaseManager.getDBInstance(
                                    Objects.requireNonNull(fragmentWeakReference.get()))
                                    .getProcessSubmitCount(formId);

                            if (!TextUtils.isEmpty(countStr)) {
                                int count = Integer.parseInt(countStr);
                                DatabaseManager.getDBInstance(
                                        Objects.requireNonNull(fragmentWeakReference.get()))
                                        .updateProcessSubmitCount(formId, String.valueOf(++count));
                            }

                            Intent intent = new Intent();
                            if (oid != null) {
                                intent.setAction(SyncAdapterUtils.PARTIAL_FORM_REMOVED);
                            }
                            intent.setAction(SyncAdapterUtils.EVENT_FORM_SUBMITTED);
                            Context context = fragmentWeakReference.get();
                            if (context != null) {
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            }
                        }
                    }

//                    FormDisplayActivity activity = (FormDisplayActivity) fragmentWeakReference.get();
//                    if (activity != null) {
//                        activity.closeScreen(true);
//                    }

                    //Objects.requireNonNull(fragmentWeakReference.get()).onBackPressed();
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

                FragmentActivity activity = fragmentWeakReference.get();
                if (activity != null) {
                    DatabaseManager.getDBInstance(activity).insertFormSchema(form.getData());
                    Log.d(TAG, "Form schema saved in database.");
                }
            }
        }

        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            //fragmentWeakReference.get().showNextScreen(response);
        }
    }

    @Override
    public void onChoicesPopulated(String response, Elements elements, int pageIndex, int elementIndex, int columnIndex, long rowIndex, FormData formData, HashMap<String, String> matrixDynamicInnerMap) {

    }

    @Override
    public void onSubmitClick(String submitType, String url, String formId, String oid, List<Map<String, String>> imageUrlList) {
        FormRequestCall formRequestCall = new FormRequestCall();
        formRequestCall.setListener(this);

        switch (submitType) {
            case Constants.ONLINE_SUBMIT_FORM_TYPE:
                fragmentWeakReference.get().showProgressBar();
                formRequestCall.createFormResponse(getRequestedObject(), getMatrixDynamicValuesMap(),
                        imageUrlList, url, formId, oid, submitType);
                break;
        }
    }

    @Override
    public void onFormDetailsLoadedListener(String response) {
//        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
//            fragmentWeakReference.get().hideProgressBar();
//            fragmentWeakReference.get().getFormDataAndParse(response);
//        }
    }

    @SuppressLint("StaticFieldLeak")
    public void uploadImage(File file, String type, final String formName) {
        ImageRequestCall requestCall = new ImageRequestCall();
        requestCall.setListener(this);
        fragmentWeakReference.get().showProgressBar();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... voids) {
                requestCall.uploadImageUsingHttpURLEncoded(file, type, formName, null, null);
                return null;
            }
        }.execute();

    }
}
