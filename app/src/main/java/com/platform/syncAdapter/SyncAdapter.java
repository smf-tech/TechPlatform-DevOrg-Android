package com.platform.syncAdapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.platform.BuildConfig;
import com.platform.Platform;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.models.SujalamSuphalam.StructurePripretionData;
import com.platform.models.SujalamSuphalam.StructureVisitMonitoringData;
import com.platform.models.Operator.OperatorRequestResponseModel;
import com.platform.models.common.Microservice;
import com.platform.models.events.CommonResponse;
import com.platform.models.forms.FormData;
import com.platform.models.forms.FormResult;
import com.platform.models.login.Login;
import com.platform.models.pm.ProcessData;
import com.platform.utility.Constants;
import com.platform.utility.VolleyMultipartRequest;
import com.platform.view.activities.OperatorMeterReadingActivity;
import com.platform.utility.Urls;
import com.platform.utility.Util;
import com.platform.utility.VolleyMultipartRequest;
import com.platform.view.activities.StructurePripretionsActivity;
import com.platform.view.activities.StructureVisitMonitoringActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.platform.presenter.PMFragmentPresenter.getAllNonSyncedSavedForms;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_FORM_SUBMITTED;
import static com.platform.utility.Constants.Form.EXTRA_FORM_ID;
import static com.platform.utility.Util.getLoginObjectFromPref;
import static com.platform.utility.Util.getUserObjectFromPref;

@SuppressWarnings({"unused", "CanBeFinal"})
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private RequestQueue rQueue;
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
        uploadImage("");


        syncStructureVisitMonitoring();
        syncStructurePripretion();
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

// operator record sync
//api call to upload record -
private void uploadImage(String receivedImage) {

    final String upload_URL = BuildConfig.BASE_URL + Urls.OperatorApi.MACHINE_WORKLOG;

    List<OperatorRequestResponseModel> list = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().getAllProcesses();
    if (list.size()>0){
        Log.e("sync--", "---" + new Gson().toJson(list.get(0)));
    OperatorRequestResponseModel operatorRequestResponseModel = list.get(0);
    String imageToSend = operatorRequestResponseModel.getImage();
    VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
            new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    rQueue.getCache().clear();
                    try {
                        String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        Toast.makeText(getContext(), jsonString, Toast.LENGTH_LONG).show();
                        Log.d("response Received -", jsonString);

                        DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().deleteSinglSynccedOperatorRecord(list.get(0).get_id());
                        if (list.size() > 0) {
                            uploadImage("");
                        } else {
                            Toast.makeText(getContext(), "Sync Complete", Toast.LENGTH_LONG).show();
                        }

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put("formData", new Gson().toJson(operatorRequestResponseModel));
            params.put("imageArraySize", String.valueOf("1"));//add string parameters
            return params;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json, text/plain, */*");
            headers.put("Content-Type", getBodyContentType());

            Login loginObj = getLoginObjectFromPref();
            if (loginObj != null && loginObj.getLoginData() != null &&
                    loginObj.getLoginData().getAccessToken() != null) {
                headers.put(Constants.Login.AUTHORIZATION,
                        "Bearer " + loginObj.getLoginData().getAccessToken());
                if (getUserObjectFromPref().getOrgId()!=null) {
                    headers.put("orgId", getUserObjectFromPref().getOrgId());
                }
                if (getUserObjectFromPref().getProjectIds()!=null) {
                    headers.put("projectId", getUserObjectFromPref().getProjectIds().get(0).getId());
                }
                if (getUserObjectFromPref().getRoleIds()!=null) {
                    headers.put("roleId", getUserObjectFromPref().getRoleIds());
                }
            }
            return headers;
        }

        @Override
        protected Map<String, DataPart> getByteData() {
            Map<String, DataPart> params = new HashMap<>();
            Drawable drawable = null;
            //   Iterator myVeryOwnIterator = imageHashmap.keySet().iterator();
            //for (int i = 0;i<imageHashmap.size(); i++)
            {
                // String key=(String)myVeryOwnIterator.next();
                if (TextUtils.isEmpty(imageToSend)) {
                    params.put("image0", new DataPart("image0", new byte[0],
                            "image/jpeg"));
                } else {
                    drawable = new BitmapDrawable(getContext().getResources(), imageToSend);

                    params.put("image0", new DataPart("image0", getFileDataFromDrawable(drawable),
                            "image/jpeg"));
                }
            }
            return params;
        }
    };

    volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
            3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    rQueue = Volley.newRequestQueue(getContext());
    rQueue.add(volleyMultipartRequest);
}
}

    private byte[] getFileDataFromDrawable(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void syncStructureVisitMonitoring() {
        List<StructureVisitMonitoringData> structureVisitMonitoringList = new ArrayList<>();
        structureVisitMonitoringList.addAll(DatabaseManager.getDBInstance(Platform.getInstance())
                .getStructureVisitMonitoringDataDao().getAllStructure());

        for (final StructureVisitMonitoringData data : structureVisitMonitoringList) {
            submitVisitData(data, 1);
        }

    }

    private void submitVisitData(StructureVisitMonitoringData requestData, int imageCount) {

        final String upload_URL = BuildConfig.BASE_URL + Urls.SSModule.STRUCTURE_VISITE_MONITORING;

        Log.d("VISITE_MONITORING req:", new Gson().toJson(requestData));

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        rQueue.getCache().clear();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            Log.d("VISITE_MONITORING resp:", jsonString);

                            CommonResponse res = new Gson().fromJson(jsonString, CommonResponse.class);
                            if (res.getStatus() == 200) {
                                DatabaseManager.getDBInstance(Platform.getInstance()).getStructureVisitMonitoringDataDao()
                                        .delete(requestData.getId());
                            }

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Log.d("VISITE_MONITORING exp:", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VISITE_MONITORING err:", error.getMessage());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", new Gson().toJson(requestData));
                params.put("imageArraySize", String.valueOf(imageCount));//add string parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json, text/plain, */*");
                headers.put("Content-Type", getBodyContentType());

                Login loginObj = getLoginObjectFromPref();
                if (loginObj != null && loginObj.getLoginData() != null &&
                        loginObj.getLoginData().getAccessToken() != null) {
                    headers.put(Constants.Login.AUTHORIZATION,
                            "Bearer " + loginObj.getLoginData().getAccessToken());
                    if (getUserObjectFromPref().getOrgId()!=null) {
                        headers.put("orgId", getUserObjectFromPref().getOrgId());
                    }
                    if (getUserObjectFromPref().getProjectIds()!=null) {
                        headers.put("projectId", getUserObjectFromPref().getProjectIds().get(0).getId());
                    }
                    if (getUserObjectFromPref().getRoleIds()!=null) {
                        headers.put("roleId", getUserObjectFromPref().getRoleIds());
                    }
                }
                return headers;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                Drawable drawable = null;
                drawable = new BitmapDrawable(getContext().getResources(), requestData.getStructureImage());
                params.put("image0", new DataPart("image0", getFileDataFromDrawable(drawable),
                        "image/jpeg"));
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(volleyMultipartRequest);
    }

    private void syncStructurePripretion() {
        List<StructurePripretionData> structureVisitMonitoringList = new ArrayList<>();
        structureVisitMonitoringList.addAll(DatabaseManager.getDBInstance(Platform.getInstance())
                .getStructurePripretionDataDao().getAllStructure());

        for (final StructurePripretionData data : structureVisitMonitoringList) {
            submitPripretionData(data, 2);
        }

    }

    private void submitPripretionData(StructurePripretionData requestData, int imageCount) {

        final String upload_URL = BuildConfig.BASE_URL + Urls.SSModule.STRUCTURE_PREPARATION;

        Log.d("STR_PREPARATION req:", new Gson().toJson(requestData));

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        rQueue.getCache().clear();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            Log.d("STR_PREPARATION res:", jsonString);

                            CommonResponse res = new Gson().fromJson(jsonString, CommonResponse.class);
                            if (res.getStatus() == 200) {
                                DatabaseManager.getDBInstance(Platform.getInstance()).getStructurePripretionDataDao()
                                        .delete(requestData.getId());
                            }

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Log.d("STR_PREPARATION exp:", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("STR_PREPARATION exp:", error.getMessage());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", new Gson().toJson(requestData));
                params.put("imageArraySize", String.valueOf(imageCount));//add string parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json, text/plain, */*");
                headers.put("Content-Type", getBodyContentType());

                Login loginObj = getLoginObjectFromPref();
                if (loginObj != null && loginObj.getLoginData() != null &&
                        loginObj.getLoginData().getAccessToken() != null) {
                    headers.put(Constants.Login.AUTHORIZATION,
                            "Bearer " + loginObj.getLoginData().getAccessToken());
                    if (getUserObjectFromPref().getOrgId() != null) {
                        headers.put("orgId", getUserObjectFromPref().getOrgId());
                    }
                    if (getUserObjectFromPref().getProjectIds() != null) {
                        headers.put("projectId", getUserObjectFromPref().getProjectIds().get(0).getId());
                    }
                    if (getUserObjectFromPref().getRoleIds() != null) {
                        headers.put("roleId", getUserObjectFromPref().getRoleIds());
                    }
                }
                return headers;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                Drawable drawable = new BitmapDrawable(getContext().getResources(), requestData.getStructureImg1());
                params.put("Structure0", new DataPart("Structure0", getFileDataFromDrawable(drawable),
                        "image/jpeg"));
                Drawable drawable1 = new BitmapDrawable(getContext().getResources(), requestData.getStructureImg1());
                params.put("Structure1", new DataPart("Structure1", getFileDataFromDrawable(drawable1),
                        "image/jpeg"));
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(volleyMultipartRequest);
    }




}