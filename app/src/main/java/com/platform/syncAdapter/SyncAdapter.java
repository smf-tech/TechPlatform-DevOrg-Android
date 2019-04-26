package com.platform.syncAdapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.models.common.Microservice;
import com.platform.models.forms.FormData;
import com.platform.models.forms.FormResult;
import com.platform.models.login.Login;
import com.platform.models.pm.ProcessData;
import com.platform.utility.Constants;

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
import java.util.List;

import static com.platform.presenter.PMFragmentPresenter.getAllNonSyncedSavedForms;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_FORM_SUBMITTED;
import static com.platform.utility.Constants.Form.EXTRA_FORM_ID;
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
            for (final FormResult form : savedForms) {
                if (form.getFormStatus() == SyncAdapterUtils.FormStatus.UN_SYNCED) {
                    submitForm(form);
                }
            }
        }
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

            String requestObject = form.getResult();

            JSONObject object = new JSONObject(requestObject);

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

                updateForm(form, response, requestObject);

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
            if (route.contains(Constants.FormDynamicKeys.FORM_ID))
                route = route.replace(Constants.FormDynamicKeys.FORM_ID, formSchema.getId());
            if (!TextUtils.isEmpty(baseUrl) && !TextUtils.isEmpty(route)) {
                url = getContext().getResources().getString(R.string.form_field_mandatory,
                        baseUrl, route);
            }
        }
        return url;
    }

    private synchronized void updateForm(final FormResult form, final String response,
                                         final String requestObjectString) {
        try {

            JSONObject outerObject = new JSONObject(response);
            JSONObject requestObject = new JSONObject(requestObjectString);

            if (outerObject.has(Constants.RESPONSE_DATA)) {
                JSONObject dataObject = outerObject.getJSONObject(Constants.RESPONSE_DATA);
                JSONObject idObject = dataObject.getJSONObject(Constants.FormDynamicKeys._ID);

                requestObject.put(Constants.FormDynamicKeys._ID, idObject);
                requestObject.put(Constants.FormDynamicKeys.FORM_TITLE,
                        dataObject.getString(Constants.FormDynamicKeys.FORM_TITLE));

                requestObject.put(Constants.FormDynamicKeys.FORM_ID, form.getFormId());
                requestObject.put(Constants.FormDynamicKeys.UPDATED_DATE_TIME,
                        dataObject.getString(Constants.FormDynamicKeys.UPDATED_DATE_TIME));

                requestObject.put(Constants.FormDynamicKeys.CREATED_DATE_TIME,
                        dataObject.getString(Constants.FormDynamicKeys.CREATED_DATE_TIME));

                FormResult result = new FormResult();
                result.setFormName(form.getFormName());
                result.set_id(idObject.getString(Constants.FormDynamicKeys.OID));
                result.setFormId(form.getFormId());
                String date = dataObject.getString(Constants.FormDynamicKeys.CREATED_DATE_TIME);
                result.setFormTitle(dataObject.getString(Constants.FormDynamicKeys.FORM_TITLE));
                result.setResult(requestObject.toString());

                result.setFormStatus(SyncAdapterUtils.FormStatus.SYNCED);
                result.setOid(idObject.getString(Constants.FormDynamicKeys.OID));

                DatabaseManager.getDBInstance(getContext()).insertFormResult(result);

                form.setFormStatus(SyncAdapterUtils.FormStatus.DELETED);
                DatabaseManager.getDBInstance(getContext()).updateFormResult(form);
            }


            updateFormSubmittedCount(form);

            sendBroadCast(form.getFormId(), SyncAdapterUtils.EVENT_SYNC_COMPLETED);

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void updateFormSubmittedCount(final FormResult form) {
        ProcessData processData = DatabaseManager.getDBInstance(getContext())
                .getProcessData(form.getFormId());
        String submitCount = processData.getSubmitCount();
        int count = 0;
        if (!TextUtils.isEmpty(submitCount)) {
            count = Integer.parseInt(submitCount);
        }

        count++;
        List<String> formResults = DatabaseManager.getDBInstance(getContext())
                .getAllFormResults(processData.getId());
        if (count == formResults.size()) {
            submitCount = String.valueOf(count);
        } else {
            submitCount = String.valueOf(formResults.size());
        }
        DatabaseManager.getDBInstance(getContext())
                .updateProcessSubmitCount(processData.getId(), submitCount);
    }

    private void sendBroadCast(final String form, final String syncEvent) {
        Intent intent = new Intent();
        intent.setAction(syncEvent);
        if (syncEvent.equals(SyncAdapterUtils.EVENT_SYNC_COMPLETED)) {
            intent.setAction(EVENT_FORM_SUBMITTED);
            intent.putExtra(EXTRA_FORM_ID, form);
        }
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