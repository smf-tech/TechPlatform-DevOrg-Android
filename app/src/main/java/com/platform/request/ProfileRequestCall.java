package com.platform.request;

import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.platform.BuildConfig;
import com.platform.Platform;
import com.platform.listeners.ProfileRequestCallListener;
import com.platform.models.login.Login;
import com.platform.models.profile.UserLocation;
import com.platform.models.user.UserInfo;
import com.platform.utility.Constants;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.PreferenceHelper;
import com.platform.utility.Urls;
import com.platform.utility.Util;
import com.platform.utility.VolleyEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.platform.utility.Util.getLoginObjectFromPref;

public class ProfileRequestCall {

    @SuppressWarnings("CanBeFinal")
    private Gson gson;
    private ProfileRequestCallListener listener;
    private final String TAG = ProfileRequestCall.class.getName();
    private File mImageFile;

    public ProfileRequestCall() {
        gson = new GsonBuilder().serializeNulls().create();
    }

    public void setListener(ProfileRequestCallListener listener) {
        this.listener = listener;
    }

    public void getOrganizations() {
        Response.Listener<JSONObject> orgSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.i(TAG, "API Org Response:" + res);
                    listener.onOrganizationsFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        };

        Response.ErrorListener orgErrorListener = error -> listener.onErrorListener(error);

        final String getOrgUrl = BuildConfig.BASE_URL + Urls.Profile.GET_ORGANIZATION;
        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getOrgUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                orgSuccessListener,
                orgErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void getOrganizationProjects(String orgId) {
        Response.Listener<JSONObject> orgProjectsSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.i(TAG, "API Org projects Response:" + res);
                    listener.onOrganizationProjectsFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        };

        Response.ErrorListener orgProjectsErrorListener = error -> listener.onErrorListener(error);

        final String getOrgProjectUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_ORGANIZATION_PROJECTS, orgId);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getOrgProjectUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                orgProjectsSuccessListener,
                orgProjectsErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void getOrganizationRoles(String orgId) {
        Response.Listener<JSONObject> orgRolesSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.i(TAG, "API Org roles Response:" + res);
                    listener.onOrganizationRolesFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        };

        Response.ErrorListener orgRolesErrorListener = error -> listener.onErrorListener(error);

        final String getOrgProjectUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_ORGANIZATION_ROLES, orgId);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getOrgProjectUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                orgRolesSuccessListener,
                orgRolesErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void getJurisdictionLevelData(String orgId, String jurisdictionTypeId, String levelName) {
        Response.Listener<JSONObject> jurisdictionSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.i(TAG, "API Jurisdiction Response:" + res);
                    listener.onJurisdictionFetched(res, levelName);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        };

        Response.ErrorListener jurisdictionErrorListener = error -> listener.onErrorListener(error);

        final String getStateUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_JURISDICTION_LEVEL_DATA, orgId, jurisdictionTypeId, levelName);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getStateUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                jurisdictionSuccessListener,
                jurisdictionErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void submitUserProfile(UserInfo userInfo) {
        Response.Listener<JSONObject> profileSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.i(TAG, "API submit profile Response:" + res);
                    listener.onProfileUpdated(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener("");
            }
        };

        Response.ErrorListener profileErrorListener = error -> listener.onErrorListener(error);

        final String submitProfileUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.SUBMIT_PROFILE, userInfo.getUserMobileNumber());

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.PUT,
                submitProfileUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                profileSuccessListener,
                profileErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(createBodyParams(userInfo));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void uploadBitmap(final Bitmap bitmap) {
        VolleyEntity volleyMultipartRequest = new VolleyEntity(Request.Method.POST, BuildConfig.BASE_URL + Urls.Profile.UPLOAD_IMAGE,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(new String(response.data));
                        Log.e(TAG, "onResponse: " + obj.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, "onResponse#error: " + error.getMessage());
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "multipart/form-data");

                Login loginObj = getLoginObjectFromPref();
                if (loginObj != null && loginObj.getLoginData() != null &&
                        loginObj.getLoginData().getAccessToken() != null) {
                    params.put(Constants.Login.AUTHORIZATION,
                            "Bearer " + loginObj.getLoginData().getAccessToken());
                }
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("type", "profile");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap), "multipart/form-data"));
                return params;
            }
        };
        volleyMultipartRequest.setHeaderParams(getHeaders());

        Platform.getInstance().getVolleyRequestQueue().add(volleyMultipartRequest);
    }

    public void uploadImageUsingHttpURLEncoded(File file) {
        final String twoHyphens = "--";
        final String lineEnd = "\r\n";
        final String boundary = "WebKitFormBoundary7MA4YWxkTrZu0gW";
        String response;

        FileInputStream fileInputStream = null;
        DataOutputStream dos = null;
        try {
            fileInputStream = new FileInputStream(file);
            URL connectURL = new URL(BuildConfig.BASE_URL + Urls.Profile.UPLOAD_IMAGE);
            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");

            Login loginObj = getLoginObjectFromPref();
            if (loginObj != null && loginObj.getLoginData() != null &&
                    loginObj.getLoginData().getAccessToken() != null) {
                conn.setRequestProperty(Constants.Login.AUTHORIZATION,
                        "Bearer " + loginObj.getLoginData().getAccessToken());
            }
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"type\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("profile");
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + file.getName() + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            int bytesAvailable = fileInputStream.available();

            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];

            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            fileInputStream.close();
            dos.flush();

            int ch;
            InputStream errorStream = conn.getErrorStream();
            if (errorStream != null) {
                StringBuilder b = new StringBuilder();
                while ((ch = errorStream.read()) != -1) {
                    b.append((char) ch);
                }

                response = b.toString();
                listener.onFailureListener(response);
                Log.i("Error#Response", response);
                return;
            }

            InputStream is = conn.getInputStream();

            StringBuilder b = new StringBuilder();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            response = b.toString();
            Log.i("Success#Response", response);

            listener.onImageUploadedListener(response);

        } catch (IOException e) {
            e.printStackTrace();
            listener.onFailureListener(e.getMessage());
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                    if (dos != null) {
                        dos.flush();
                        dos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onFailureListener(e.getMessage());
                }
            }
        }

    }

    private byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private JsonObject createBodyParams(UserInfo userInfo) {
        JsonObject body = new JsonObject();
        if (userInfo != null) {
            try {
                body.addProperty(Constants.Login.USER_NAME, userInfo.getUserName());
                body.addProperty(Constants.Login.USER_F_NAME, userInfo.getUserFirstName());
                body.addProperty(Constants.Login.USER_M_NAME, userInfo.getUserMiddleName());
                body.addProperty(Constants.Login.USER_L_NAME, userInfo.getUserLastName());
                body.addProperty(Constants.Login.USER_BIRTH_DATE, userInfo.getUserBirthDate());
                body.addProperty(Constants.Login.USER_EMAIL, userInfo.getUserEmailId());
                body.addProperty(Constants.Login.USER_GENDER, userInfo.getUserGender());
                body.addProperty(Constants.Login.USER_ORG_ID, userInfo.getOrgId());
                body.addProperty(Constants.Login.USER_ASSOCIATE_TYPE, userInfo.getType());
                body.addProperty(Constants.Login.USER_ROLE_ID, userInfo.getRoleIds());
                body.addProperty(Constants.Login.USER_PROFILE_PIC, userInfo.getProfilePic());

                // Add project Ids
                JsonArray projectIdArray = new JsonArray();
                ArrayList<String> userProjectIds = userInfo.getProjectIds();
                for (String projectId : userProjectIds) {
                    projectIdArray.add(projectId);
                }
                body.add(Constants.Login.USER_PROJECTS, projectIdArray);

                // Add user location
                UserLocation userLocation = userInfo.getUserLocation();
                JsonObject locationObj = new JsonObject();

                JsonArray locationArray = new JsonArray();
                if (userLocation.getStateId() != null) {
                    for (String stateId : userLocation.getStateId()) {
                        locationArray.add(stateId);
                    }
                    locationObj.add(Constants.Location.STATE, locationArray);
                }

                locationArray = new JsonArray();
                if (userLocation.getDistrictIds() != null) {
                    for (String districtId : userLocation.getDistrictIds()) {
                        locationArray.add(districtId);
                    }
                    locationObj.add(Constants.Location.DISTRICT, locationArray);
                }

                locationArray = new JsonArray();
                if (userLocation.getTalukaIds() != null) {
                    for (String talukaId : userLocation.getTalukaIds()) {
                        locationArray.add(talukaId);
                    }
                    locationObj.add(Constants.Location.TALUKA, locationArray);
                }

                locationArray = new JsonArray();
                if (userLocation.getVillageIds() != null) {
                    for (String villageId : userLocation.getVillageIds()) {
                        locationArray.add(villageId);
                    }
                    locationObj.add(Constants.Location.VILLAGE, locationArray);
                }

                locationArray = new JsonArray();
                if (userLocation.getClusterIds() != null) {
                    for (String clusterId : userLocation.getClusterIds()) {
                        locationArray.add(clusterId);
                    }
                    locationObj.add(Constants.Location.CLUSTER, locationArray);
                }

                body.add(Constants.Login.USER_LOCATION, locationObj);

                PreferenceHelper preferenceHelper = new PreferenceHelper(Platform.getInstance());
                String token = preferenceHelper.getString(PreferenceHelper.TOKEN);
                body.addProperty(Constants.Login.USER_FIREBASE_ID, token);

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        Log.i(TAG, "BODY: " + body);
        return body;
    }

    private Map<String, Object> getParams() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("image", mImageFile);
        parameters.put("type", "profile");
        return parameters;
    }

    private static Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "multipart/form-data");

        Login loginObj = getLoginObjectFromPref();
        if (loginObj != null && loginObj.getLoginData() != null &&
                loginObj.getLoginData().getAccessToken() != null) {
            headers.put(Constants.Login.AUTHORIZATION,
                    "Bearer " + loginObj.getLoginData().getAccessToken());
        }

        return headers;
    }
}
