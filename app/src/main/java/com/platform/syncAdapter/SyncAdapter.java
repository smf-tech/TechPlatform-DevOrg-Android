package com.platform.syncAdapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.models.common.Microservice;
import com.platform.models.forms.FormData;
import com.platform.models.forms.FormResult;
import com.platform.models.login.Login;
import com.platform.utility.Constants;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.platform.presenter.PMFragmentPresenter.getAllNonSyncedSavedForms;
import static com.platform.utility.Constants.Form.EXTRA_FORM_ID;
import static com.platform.utility.Util.getFormCategoryForSyncFromPref;
import static com.platform.utility.Util.getLoginObjectFromPref;

@SuppressWarnings({"unused", "CanBeFinal"})
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = SyncAdapter.class.getSimpleName();

    SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

    }

    public SyncAdapter(final Context context, final boolean autoInitialize,
                       final boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s,
                              ContentProviderClient contentProviderClient,
                              SyncResult syncResult) {

        Log.i(TAG, "onPerformSync: \n");
        syncSavedForms();
    }

    private void syncSavedForms() {
        List<FormResult> savedForms = getAllNonSyncedSavedForms(getContext());

        if (savedForms != null) {
            String formSyncCategory = getFormCategoryForSyncFromPref();
            /*for (final SavedForm form : savedForms) {
                if (!form.isSynced()) {
                    if (form.getFormCategory().equals(formSyncCategory) || formSyncCategory.isEmpty()) {
                        submitForm(form);
                    }
                }
            }*/
            for (final FormResult form : savedForms) {
                if (form.getFormStatus() == SyncAdapterUtils.FormStatus.UN_SYNCED) {
                    if (form.getFormCategory().equals(formSyncCategory) || formSyncCategory.isEmpty()) {
                        submitForm(form);
                    }
                }
            }
        }
    }

    public void createFormResponse(HashMap<String, String> requestObjectMap,
                                   final Map<String, String> uploadedImageUrlList,
                                   String postUrl, FormResult form) {

        Response.Listener<JSONObject> createFormResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    updateForm(form);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                sendBroadCast(form.getFormId(), SyncAdapterUtils.EVENT_SYNC_FAILED);
            }
        };

        Response.ErrorListener createFormErrorListener = error ->
                sendBroadCast(form.getFormId(), SyncAdapterUtils.EVENT_SYNC_FAILED);

        Gson gson = new GsonBuilder().serializeNulls().create();

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(Request.Method.POST,
                postUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                createFormResponseListener,
                createFormErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(getFormRequest(requestObjectMap, uploadedImageUrlList));
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    @NonNull
    private JsonObject getFormRequest(HashMap<String, String> requestObjectMap, final Map<String, String> imageUrls) {
        JsonObject requestObject = new JsonObject();
        for (Map.Entry<String, String> entry : requestObjectMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d(TAG, "Request object key " + key + " value " + value);
            requestObject.addProperty(key, value);
        }

        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (final Map.Entry<String, String> entry : imageUrls.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Log.d(TAG, "Request object key " + key + " value " + value);
                requestObject.addProperty(key, value);
            }
        }

        return requestObject;
    }

    private void submitForm(final FormResult form) {


        try {
            URL formUrl = new URL(getFormUrl(form));

            Login loginObj = getLoginObjectFromPref();
            if (loginObj == null || loginObj.getLoginData() == null ||
                    loginObj.getLoginData().getAccessToken() == null) {
                return;
            }

            String accessToken = "Bearer " + loginObj.getLoginData().getAccessToken();

            HttpURLConnection connection = (HttpURLConnection) formUrl.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json, text/plain, */*");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty(Constants.Login.AUTHORIZATION, accessToken);
            connection.setChunkedStreamingMode(0);
            connection.connect();

            String requestObject = form.getRequestObject();

            JSONObject object = new JSONObject();
            object.put("response", new JSONObject(requestObject));

            OutputStream out = new BufferedOutputStream(connection.getOutputStream());
            writeStream(object.toString(), out);

            InputStream errorStream = connection.getErrorStream();
            if (errorStream != null) {
                String response = readStream(errorStream);
                Log.e(TAG, "Response \n" + response);

                sendBroadCast(form.getFormId(), SyncAdapterUtils.EVENT_SYNC_FAILED);

            } else {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                String response = readStream(in);

                updateForm(form);

                Log.i(TAG, "Response \n" + response);
                Log.i(TAG, "Form Synced");
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    private String getFormUrl(final FormResult form) {
        String formId = form.getFormId();
        String url = null;

        FormData formSchema = DatabaseManager.getDBInstance(getContext()).getFormSchema(formId);
        if (formSchema != null && formSchema.getMicroService() != null) {
            Microservice microService = formSchema.getMicroService();

            String baseUrl = microService.getBaseUrl();
            String route = microService.getRoute();
            if (route.contains("form_id"))
                route = route.replace("form_id", formSchema.getId());
            if (!TextUtils.isEmpty(baseUrl) && !TextUtils.isEmpty(route)) {
                url = getContext().getResources().getString(R.string.form_field_mandatory,
                        baseUrl, route);
            }
        }
        return url;
    }

    private void updateForm(final FormResult form) {
        form.setFormStatus(SyncAdapterUtils.FormStatus.SYNCED);
        DatabaseManager.getDBInstance(getContext()).updateFormResult(form);

        sendBroadCast(form.getFormId(), SyncAdapterUtils.EVENT_SYNC_COMPLETED);
    }

    private void sendBroadCast(final String form, final String syncEvent) {
        Intent intent = new Intent(syncEvent);
        if (syncEvent.equals(SyncAdapterUtils.EVENT_SYNC_COMPLETED))
            intent.putExtra(EXTRA_FORM_ID, form);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    private String readStream(final InputStream stream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        for (String line; (line = r.readLine()) != null; ) {
            total.append(line).append('\n');
        }
        return total.toString();
    }

    private void writeStream(String data, final OutputStream out) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}