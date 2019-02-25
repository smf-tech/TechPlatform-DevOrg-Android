package com.platform.syncAdapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.platform.BuildConfig;
import com.platform.database.DatabaseManager;
import com.platform.models.SavedForm;
import com.platform.models.login.Login;
import com.platform.utility.Constants;
import com.platform.utility.Urls;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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
        List<SavedForm> savedForms;
        if (getContext() == null) {
            savedForms = getAllNonSyncedSavedForms();
        } else {
            savedForms = getAllNonSyncedSavedForms(getContext());
        }

        if (savedForms != null) {
            String formSyncCategory = getFormCategoryForSyncFromPref();
            for (final SavedForm form : savedForms) {
                if (!form.isSynced()) {
                    if (form.getFormCategory().equals(formSyncCategory) || formSyncCategory.isEmpty()) {
                        try {
                            submitForm(form);
                        } catch (MalformedURLException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }
            }
        }
    }

    private void submitForm(final SavedForm form) throws MalformedURLException {
        URL url = new URL(BuildConfig.BASE_URL + String.format(Urls.PM.CREATE_FORM,
                form.getFormId()));

        try {

            Login loginObj = getLoginObjectFromPref();
            if (loginObj == null || loginObj.getLoginData() == null ||
                    loginObj.getLoginData().getAccessToken() == null) {
                return;
            }

            String accessToken = "Bearer " + loginObj.getLoginData().getAccessToken();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

                sendBroadCast(form, SyncAdapterUtils.EVENT_SYNC_FAILED);

            } else {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                String response = readStream(in);

                updateForm(form);

                Log.i(TAG, "Response \n" + response);
                Log.i(TAG, "Form Synced");
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void updateForm(final SavedForm form) {
        form.setSynced(true);
        DatabaseManager.getDBInstance(getContext()).updateFormObject(form);

        sendBroadCast(form, SyncAdapterUtils.EVENT_SYNC_COMPLETED);
    }

    private void sendBroadCast(final SavedForm form, final String syncEvent) {
        Intent intent = new Intent(syncEvent);
        if (syncEvent.equals(SyncAdapterUtils.EVENT_SYNC_COMPLETED))
            intent.putExtra(EXTRA_FORM_ID, form.id);
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